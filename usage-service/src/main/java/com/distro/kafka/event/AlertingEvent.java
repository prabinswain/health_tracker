package com.distro.kafka.event;

import lombok.Builder;

@Builder
public record AlertingEvent(
        Long userId,
        String message,
        double thershold,
        double energyConsumed,
        String email
) {
}
