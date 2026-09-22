package com.distro.user_service.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {

    private Long id;

    private int status;

    private String message;

    private String name;
}
