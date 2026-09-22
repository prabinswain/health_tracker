package com.distro.user_service.service;

import com.distro.user_service.dto.request.UserRequestDto;
import com.distro.user_service.dto.response.UserResponseDto;
import com.distro.user_service.entity.User;
import com.distro.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;


    public UserResponseDto createUser(UserRequestDto userRequestDto) {

        log.info("A User getting register.");
        User user = buildUserObject(userRequestDto);
        user = userRepository.save(user);
        return buildUserObject(user);

    }


    private User buildUserObject(UserRequestDto userRequestDto) {
        return User.builder()
                .name(userRequestDto.getName())
                .email(userRequestDto.getEmail())
                .address(userRequestDto.getAddress())
                .alerting(false)
                .energyAlertingThreshold(0.0)
                .surname(userRequestDto.getSurname())
                .build();
    }

    private UserResponseDto buildUserObject(User user) {
        return UserResponseDto.builder()
                .name(user.getName())
                .id(user.getId())
                .status(201)
                .message("User created.")
                .build();
    }
}
