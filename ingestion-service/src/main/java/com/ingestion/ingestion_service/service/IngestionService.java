package com.ingestion.ingestion_service.service;

import com.ingestion.ingestion_service.dto.EnergyUsageDto;
import com.ingestion.kafka.event.EventUsageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class IngestionService {

    private final KafkaTemplate<String, EventUsageEvent> kafkaTemplate;

    public IngestionService(KafkaTemplate<String, EventUsageEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void ingestEnergyUsage(EnergyUsageDto energyUsageDto) {
        // Convert DTO to event
        EventUsageEvent event = EventUsageEvent.builder()
                .deviceId(energyUsageDto.deviceId())
                .timestamp(energyUsageDto.timestamp())
                .energyConsumed(energyUsageDto.energyConsumed())
                .build();

        // send to kafka topic
        kafkaTemplate.send("energy-usage", event);
        log.info("Ingested energy usage event: {}", event);
    }
}
