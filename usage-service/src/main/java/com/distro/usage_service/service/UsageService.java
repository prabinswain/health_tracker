package com.distro.usage_service.service;

import com.distro.kafka.event.AlertingEvent;
import com.distro.kafka.event.EnergyUsageEvent;
import com.distro.usage_service.client.DeviceClient;
import com.distro.usage_service.client.UserClient;
import com.distro.usage_service.dto.DeviceDto;
import com.distro.usage_service.dto.UserDto;
import com.distro.usage_service.model.DeviceEnergy;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.client.domain.User;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.sun.jdi.LongValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UsageService {

    @Value("${influx.url}")
    private String influxUrl;
    @Value("${influx.org}")
    private String influxOrg;
    @Value("${influx.bucket}")
    private String influxBucket;

    private InfluxDBClient influxDBClient;
    private DeviceClient deviceClient;
    private UserClient userClient;
    private final KafkaTemplate<String, AlertingEvent> kafkaTemplate;


    public UsageService(InfluxDBClient influxDBClient, DeviceClient deviceClient
            , UserClient userClient
            , KafkaTemplate<String, AlertingEvent> kafkaTemplate) {
        this.influxDBClient = influxDBClient;
        this.deviceClient = deviceClient;
        this.userClient = userClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "energy-usage", groupId = "usage-service")
    public void energyUsageEvent(EnergyUsageEvent energyUsageEvent) {
        // log.info("Received energy usage event: {}", energyUsageEvent);

        // process energy usage event and store in influx DB
        Point point = Point.measurement("energy-usage")
                .addTag("deviceId", String.valueOf(energyUsageEvent.deviceId()))
                .addField("energyConsumed", energyUsageEvent.energyConsumed())
                .time(energyUsageEvent.timestamp(), WritePrecision.MS);

        influxDBClient.getWriteApiBlocking().writePoint(influxBucket, influxOrg, point);
    }

    // After consuming, we needs to aggregate data based on deviceId (each user)
    // if certain thershold passes we needs to send an alert to kafka
    @Scheduled(cron = "*/10 * * * * *")
    public void aggregateDeviceEnergyUsage() {
        final Instant currentTime = Instant.now();
        final Instant oneHourAgo = currentTime.minusSeconds(3600);

        // now to retrieve and aggregate existing data.
        String energyUsageQuery = String.format("""
                from(bucket: "%s")
                  |> range(start: time(v: "%s"), stop: time(v: "%s"))
                  |> filter(fn: (r) => r["_measurement"] == "energy-usage")
                  |> filter(fn: (r) => r["_field"] == "energyConsumed")
                  |> group(columns: ["deviceId"])
                  |> sum(column: "_value")
                """, influxBucket, oneHourAgo.toString(), currentTime);

        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> fluxTables = queryApi.query(energyUsageQuery, influxOrg);

        // transfer tabular data to own model
        List<DeviceEnergy> deviceEnergies = new ArrayList<>();

        for (FluxTable table : fluxTables) {
            for (FluxRecord fluxRecord : table.getRecords()) {
                String deviceIdStr = (String) fluxRecord.getValueByKey("deviceId");
                Double energyConsumed = fluxRecord.getValueByKey("_value") instanceof Number ?
                        ((Number) fluxRecord.getValueByKey("_value")).doubleValue() : 0.0;

                deviceEnergies.add(
                        DeviceEnergy.builder()
                                .deviceId(Long.valueOf(deviceIdStr))
                                .energyConsumed(energyConsumed)
                                .build()
                );
                log.debug("records added in deviceEnergies list");
            }
        }

        log.info("Aggregated device energies over the past hour: {}", deviceEnergies);
        for (DeviceEnergy deviceEnergy : deviceEnergies) {

            try{
                final DeviceDto deviceResponse = deviceClient.getDeviceById(deviceEnergy.getDeviceId());

                if (deviceResponse == null || deviceResponse.getId() == null) {
                    log.warn("Device not found for ID: {}", deviceEnergy.getDeviceId());
                    continue;
                }
                log.info("if condition failed and before setting userId with user ID: {}", deviceResponse.getUserId());
                deviceEnergy.setUserId(deviceResponse.getUserId());
            } catch (Exception e) {
                log.warn("Failed to fetch device, message {}", e.getMessage());
            }

        }

        // remove devices with null userId
        deviceEnergies.removeIf(de -> de.getUserId() == null);

        // Get user-device mapping and aggregate per user
        Map<Long, List<DeviceEnergy>> userDeviceEnergyMap =
                deviceEnergies.stream()
                        .collect(Collectors.groupingBy(DeviceEnergy::getUserId));

        log.info("User-Device Energy Map: {}", userDeviceEnergyMap);

        // get users energy consumption thresholds
        List<Long> userIds = new ArrayList<>(userDeviceEnergyMap.keySet());
        final Map<Long, Double> userThresholdMap = new HashMap<>();
        final Map<Long, String> userEmailMap = new HashMap<>();

        for (final Long userId : userIds) {
            try {
                UserDto user = userClient.getUserById(userId);
                log.info("user: {}",user);

                if (user == null || user.getId() == null) {
                    log.warn("User not found for ID: {}", userId);
                    continue;
                }

                if (!Boolean.TRUE.equals(user.getAlerting())) {
                    log.warn("Alerting is disabled or not configured for user ID: {}", userId);
//                    continue;
                }

                userThresholdMap.put(userId, user.getEnergyAlertingThreshold());
                userEmailMap.put(userId, user.getEmail());
            } catch (Exception e) {
                log.warn("Failed to fetch user for ID: {} with message {}", userId, e.getMessage());
            }
        }
        log.info("User Threshold Map: {}", userThresholdMap);


        // Check thresholds against aggregated usage
        final List<Long> alertedUsers = new ArrayList<>(userThresholdMap.keySet());

        for (final Long userId : alertedUsers) {
            final Double threshold = userThresholdMap.get(userId);
            final List<DeviceEnergy> devices = userDeviceEnergyMap.get(userId);

            final Double totalConsumption = devices.stream()
                    .mapToDouble(DeviceEnergy::getEnergyConsumed)
                    .sum();
            log.info("totalConsumption: {}, threshold:{}",totalConsumption,threshold);
            if (totalConsumption > threshold) {
                log.info("ALERT: User ID {} has exceeded the energy threshold! " +
                                "Total Consumption: {}, Threshold: {}",
                        userId, totalConsumption, threshold);
                // Put message on kafak alert-topic
                final AlertingEvent alertingEvent = AlertingEvent.builder()
                        .userId(userId)
                        .message("Energy consumption threshold exceeded")
                        .thershold(threshold)
                        .energyConsumed(totalConsumption)
                        .email(userEmailMap.get(userId))
                        .build();

                log.info(
                        "Preparing alert: userId={}, consumption={}, threshold={}",
                        userId, totalConsumption, threshold
                );
                kafkaTemplate.send("energy-alerts", alertingEvent);
            } else {
                log.info("User ID {} is within the energy threshold. " +
                                "Total Consumption: {}, Threshold: {}",
                        userId, totalConsumption, threshold);
            }



        }
    }
}
