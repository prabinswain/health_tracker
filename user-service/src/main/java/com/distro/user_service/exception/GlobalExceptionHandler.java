package com.distro.user_service.exception;

import com.distro.user_service.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFound(
            UserNotFoundException ex, WebRequest request) {

//        log.warn("User not found.");
//        ApiResponse<Void> response =
//                ApiResponse.failure(
//                        ex.getMessage(),
//                        null
//                );
//

        final ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
//                .getType(ServletUriComponentsBuilder.fromUri( java.net.URI.create()))

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(
            Exception ex) {

//        log.error("Error in exception class");
//        ApiResponse<Void> response =
//                ApiResponse.failure(
//                        ex.getMessage(),
//                        null
//                );
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        final ProblemDetail problemDetail = ProblemDetail
                .forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setDetail("Some error occured");
        problemDetail.setTitle("go");

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);

    }
}