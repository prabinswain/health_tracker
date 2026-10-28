package com.ingestion.ingestion_service.simualation;

import com.ingestion.ingestion_service.dto.EnergyUsageDto;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Component
@Slf4j
public class ParallelDataSimulation implements CommandLineRunner {

    private final RestTemplate restTemplate = new RestTemplate();
    private final Random random = new Random();

    private final ExecutorService service = Executors.newCachedThreadPool();

    @Value("${simulation.parallel-threads}")
    private int parallelThreads;
    @Value("${simulation.requests-per-interval}")
    private Integer requestsPerInterval;
    @Value("${simulation.endpoint}")
    private String ingestionEndpoint;

//    public ParallelDataSimulation(ExecutorService service) {
//        this.service = Executors.newCachedThreadPool();
//    }


    @Override
    public void run(String... args) throws Exception {
        log.info("Parallel Data simulation started..");
        ((ThreadPoolExecutor) service).setCorePoolSize(parallelThreads);
    }

    @Scheduled(fixedRateString = "${simulation.interval-ms}")
    public void mockData() {
        int batchSize = requestsPerInterval / parallelThreads;
        int reminder = requestsPerInterval % parallelThreads;

        service.submit(() -> {
            for (int j = 0; j < requestsPerInterval; j++) {
                EnergyUsageDto dto = EnergyUsageDto.builder()
                        .deviceId(random.nextLong(1, 6))
                        .energyConsumed(Math.round(random.nextDouble(0.0, 10.0) * 100.0) / 100.0)
                        .timestamp(LocalDateTime.now().atZone(
                                ZoneId.systemDefault()).toInstant())
                        .build();


                try {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);

                    HttpEntity<EnergyUsageDto> request = new HttpEntity<>(dto, headers);
                    restTemplate.postForEntity(ingestionEndpoint, request, Void.class);

                    log.info("Send mock data :{}", dto);
                } catch (Exception ex) {
                    log.error("Failed to send data: {}", ex.getMessage());
                }
            }
        });


    }

    @PreDestroy
    public void shutdown(){
        service.shutdown();
        log.info("Parallel Data simulation shutdown..");
    }
}
