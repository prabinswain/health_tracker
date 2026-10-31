package com.distro.device_service.dto.request;

import com.distro.device_service.model.DeviceType;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DeviceDto {
    private Long id;
    private String name;
    private DeviceType type;
    private String location;
    private Long userId;
}
