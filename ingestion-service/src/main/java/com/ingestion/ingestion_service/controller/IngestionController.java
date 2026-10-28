package com.ingestion.ingestion_service.controller;

import com.ingestion.ingestion_service.dto.EnergyUsageDto;
import com.ingestion.ingestion_service.service.IngestionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/ingestion")
public class IngestionController {

    private final IngestionService ingestionService;

    public IngestionController(IngestionService ingestionService){

        this.ingestionService = ingestionService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void ingestData(@RequestBody EnergyUsageDto energyUsageDto){
        ingestionService.ingestEnergyUsage(energyUsageDto);
    }
}
