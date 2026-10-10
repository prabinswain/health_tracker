package com.distro.user_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    private String name;

    private String surname;

    private String email;

    private String address;

    private Boolean alerting;

    private Double energyAlertingThreshold;
}
