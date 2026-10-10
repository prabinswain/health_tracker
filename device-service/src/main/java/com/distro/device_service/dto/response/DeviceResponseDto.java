package com.distro.device_service.dto.response;


import com.distro.device_service.model.DeviceType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class DeviceResponseDto {

    private String name;
    private DeviceType deviceType;
    private String location;
}
