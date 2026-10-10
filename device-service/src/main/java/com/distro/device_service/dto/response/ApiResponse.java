package com.distro.device_service.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private Instant timestamp;
    private String traceId;

    public static <T> ApiResponse<T> success(String message, T data, String traceId) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(Instant.now())
                .traceId(traceId)
                .build();
    }

    public static <T> ApiResponse<T> success(T data, String traceId) {
        return success("Request successful", data, traceId);
    }

    public static <T> ApiResponse<T> failure(String message, String traceId) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .timestamp(Instant.now())
                .traceId(traceId)
                .build();
    }

}
