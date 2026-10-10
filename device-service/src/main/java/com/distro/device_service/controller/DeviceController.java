package com.distro.device_service.controller;

import com.distro.device_service.dto.request.DeviceDto;
import com.distro.device_service.dto.response.ApiResponse;
import com.distro.device_service.dto.response.DeviceResponseDto;
import com.distro.device_service.service.DeviceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DeviceResponseDto>> createDevice(@RequestBody DeviceDto deviceDto) {

        DeviceResponseDto responseDto = deviceService.createDevice(deviceDto);

        ApiResponse<DeviceResponseDto> response =
                ApiResponse.success(
                        "User created successfully",
                        responseDto,
                        null
                );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DeviceResponseDto>> getUser(@PathVariable Long id) {

        DeviceResponseDto userDto = deviceService.getDevice(id);
        ApiResponse<DeviceResponseDto> response =
                ApiResponse.success(
                        "User is fetched",
                        userDto,
                        null
                );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DeviceResponseDto>> updateUser(@PathVariable Long id
            , @RequestBody DeviceDto deviceRequestDto) {

        DeviceResponseDto responseDto = deviceService.updateDevice(id, deviceRequestDto);
        ApiResponse<DeviceResponseDto> response =
                ApiResponse.success(
                        responseDto,
                        null
                );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<DeviceResponseDto>> deleteUser(@PathVariable Long id) {

        deviceService.deleteDevice(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

    }
}
