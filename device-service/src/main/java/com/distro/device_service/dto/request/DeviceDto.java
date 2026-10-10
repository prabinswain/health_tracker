package com.distro.device_service.dto.request;

import com.distro.device_service.model.DeviceType;
import lombok.*;


@Data
@Setter
@Getter
@Builder
public class DeviceDto {

    private String name;
    private DeviceType deviceType;
    private String location;

}
