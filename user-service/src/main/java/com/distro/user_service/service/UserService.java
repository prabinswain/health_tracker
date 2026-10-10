package com.distro.user_service.service;

import com.distro.user_service.dto.request.UserRequestDto;
import com.distro.user_service.dto.response.UserDto;
import com.distro.user_service.dto.response.UserResponseDto;
import com.distro.user_service.entity.User;
import com.distro.user_service.exception.UserNotFoundException;
import com.distro.user_service.helper.UserMapper;
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

//        log.info("A User getting register. name: {} ", userRequestDto.getName());
        User user = UserMapper.buildUserObject(userRequestDto);
        user = userRepository.save(user);
        return UserMapper.buildUserResponseDto(user);

    }

    public UserDto getUser(Long id) throws UserNotFoundException {

//        log.info("Getting user by ID ");
        return userRepository.findById(id)
                .map(UserMapper::buildUserDto)
                .orElseThrow(() -> new UserNotFoundException("User is not present."));
    }

    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {

//        log.info("A User getting updated. name: {} ", userRequestDto.getName());
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User is not present."));

        user.setName(userRequestDto.getName());
        user.setAddress(userRequestDto.getAddress());
        user.setEmail(userRequestDto.getEmail());
        user.setAlerting(userRequestDto.getAlerting());
        user.setSurname(userRequestDto.getSurname());

        userRepository.save(user);
        return UserMapper.buildUserResponseDto(user);

    }

    public void deleteUser(Long id) {
        log.info("A User getting deleted. id: {} ", id);
        userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User is not present."));

        userRepository.deleteById(id);
    }
}
