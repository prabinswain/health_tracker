package com.distro.usage_service.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DeviceDto {
    private Long id;
    private String name;
    private String type;
    private String location;
    private Long userId;
}
