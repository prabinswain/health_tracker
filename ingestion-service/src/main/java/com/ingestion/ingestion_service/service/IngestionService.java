package com.ingestion.ingestion_service.service;

import com.ingestion.ingestion_service.dto.EnergyUsageDto;
import com.ingestion.kafka.event.EnergyUsageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class IngestionService {

    private final KafkaTemplate<String, EnergyUsageEvent> kafkaTemplate;

    public IngestionService(KafkaTemplate<String, EnergyUsageEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void ingestEnergyUsage(EnergyUsageDto energyUsageDto) {
        // Convert DTO to event
        EnergyUsageEvent event = EnergyUsageEvent.builder()
                .deviceId(energyUsageDto.deviceId())
                .timestamp(energyUsageDto.timestamp())
                .energyConsumed(energyUsageDto.energyConsumed())
                .build();

        // send to kafka topic
        kafkaTemplate.send("energy-usage", event);
        log.info("Ingested energy usage event: {}", event);
    }
}
