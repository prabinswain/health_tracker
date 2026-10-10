package com.distro.user_service.helper;

import com.distro.user_service.dto.request.UserRequestDto;
import com.distro.user_service.dto.response.UserDto;
import com.distro.user_service.dto.response.UserResponseDto;
import com.distro.user_service.entity.User;

public class UserMapper {

    public static User buildUserObject(UserRequestDto userRequestDto) {
        return User.builder()
                .name(userRequestDto.getName())
                .email(userRequestDto.getEmail())
                .address(userRequestDto.getAddress())
                .alerting(false)
                .energyAlertingThreshold(0.0)
                .surname(userRequestDto.getSurname())
                .build();
    }

    public static UserResponseDto buildUserResponseDto(User user) {
        return UserResponseDto.builder()
                .name(user.getName())
                .id(user.getId())
                .build();
    }

    public static UserDto buildUserDto(User user) {
        return UserDto.builder()
                .name(user.getName())
                .id(user.getId())
                .address(user.getAddress())
                .surname(user.getSurname())
                .email(user.getEmail())
                .build();
    }

}
