package com.distro.user_service.controller;

import com.distro.user_service.dto.request.UserRequestDto;
import com.distro.user_service.dto.response.ApiResponse;
import com.distro.user_service.dto.response.UserDto;
import com.distro.user_service.dto.response.UserResponseDto;
import com.distro.user_service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser(@RequestBody UserRequestDto userRequestDto) {

        UserResponseDto responseDto = userService.createUser(userRequestDto);

        ApiResponse<UserResponseDto> response =
                ApiResponse.success(
                        "User created successfully",
                        responseDto,
                        null
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable Long id) {

        UserDto userDto = userService.getUser(id);
        ApiResponse<UserDto> response =
                ApiResponse.success(
                        "User is fetched",
                        userDto,
                        null
                );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(@PathVariable Long id
            , @RequestBody UserRequestDto userRequestDto) {

        UserResponseDto responseDto = userService.updateUser(id, userRequestDto);

        ApiResponse<UserResponseDto> response =
                ApiResponse.success(
                        responseDto,
                        null
                );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(null);

    }
}
