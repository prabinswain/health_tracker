package com.distro.user_service.controller;

import com.distro.user_service.UserServiceApplication;
import com.distro.user_service.dto.request.UserRequestDto;
import com.distro.user_service.dto.response.UserResponseDto;
import com.distro.user_service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto userRequestDto){
        UserResponseDto responseDto =   userService.createUser(userRequestDto);
        return new ResponseEntity(responseDto , HttpStatus.CREATED);
    }
}
