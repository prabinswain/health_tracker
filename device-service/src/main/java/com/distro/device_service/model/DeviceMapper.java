package com.distro.device_service.model;

import com.distro.device_service.dto.request.DeviceDto;
import com.distro.device_service.dto.response.DeviceResponseDto;
import com.distro.device_service.entity.Device;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DeviceMapper {


    public static Device toDevice(DeviceDto deviceDto) {

        return  Device.builder()
                .deviceType(deviceDto.getDeviceType())
                .name(deviceDto.getName())
                .location(deviceDto.getLocation())
                .build();
    }

    public static DeviceResponseDto toDeviceResponseDto(Device device) {
            return DeviceResponseDto.builder()
                    .deviceType(device.getDeviceType())
                    .name(device.getName())
                    .location(device.getLocation())
                    .build();
    }
}
