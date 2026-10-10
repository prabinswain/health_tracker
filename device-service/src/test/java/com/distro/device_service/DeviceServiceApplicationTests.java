package com.distro.device_service;

import com.distro.device_service.entity.Device;
import com.distro.device_service.model.DeviceType;
import com.distro.device_service.repository.DeviceRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DeviceServiceApplicationTests {

    public static final int NUMBER_OF_VALUES = 200;
    public static final int  USERS = 10;
    @Autowired
    public DeviceRepository deviceRepository;

    @Test
    void contextLoads() {
    }

    @Disabled
    @Test
    void createDevice() {

        final int NUMBER_OF_VALUES = 200;

        for (int i = 0; i < NUMBER_OF_VALUES; i++) {
            Device device = Device.builder()
                    .deviceType(DeviceType.values()[1%DeviceType.values().length])
                    .location("Location " + (i%3) + i)
                    .name("Device" + 1)
                    .userId((long) ((i % USERS) + 1))
                    .build();
            deviceRepository.save(device);
        }

    }

}
