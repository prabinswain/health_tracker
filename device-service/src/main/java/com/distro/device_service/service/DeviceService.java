package com.distro.device_service.service;

import com.distro.device_service.dto.request.DeviceDto;
import com.distro.device_service.dto.response.DeviceResponseDto;
import com.distro.device_service.entity.Device;
import com.distro.device_service.exception.DeviceNotFoundException;
import com.distro.device_service.model.DeviceMapper;
import com.distro.device_service.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public void deleteDevice(Long id) {

        Device device = deviceRepository.findById(id).orElseThrow(
                () -> new DeviceNotFoundException("Device is not present."));
        deviceRepository.delete(device);
    }

    public DeviceResponseDto updateDevice(Long id, DeviceDto userRequestDto) {

        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device is not present."));

        device.setName(userRequestDto.getName());
        device.setDeviceType(userRequestDto.getDeviceType());
        device.setLocation(userRequestDto.getLocation());

        device = deviceRepository.save(device);
        return DeviceMapper.toDeviceResponseDto(device);

    }

    public DeviceResponseDto getDevice(Long id) {

        return deviceRepository.findById(id)
                .map(DeviceMapper::toDeviceResponseDto)
                .orElseThrow(() -> new DeviceNotFoundException("Device is not present"));
    }

    public DeviceResponseDto createDevice(DeviceDto deviceDto) {

        Device device = deviceRepository.save(DeviceMapper.toDevice(deviceDto));
        return DeviceMapper.toDeviceResponseDto(device);
    }
}
