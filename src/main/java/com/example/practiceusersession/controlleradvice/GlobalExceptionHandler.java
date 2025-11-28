package com.example.practiceusersession.controlleradvice;

import com.example.practiceusersession.dto.GenericResponseDto;
import com.example.practiceusersession.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LoginWrongCredentialsException.class)
    public ResponseEntity<GenericResponseDto<Object>> handleLoginWrongCredentialsException(LoginWrongCredentialsException exception) {
        ResponseEntity<GenericResponseDto<Object>> response = new ResponseEntity<>(
                GenericResponseDto.builder()
                        .error(true)
                        .message(exception.getMessage())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
        return response;
    }

    @ExceptionHandler(ResendEmailNotFoundException.class)
    public ResponseEntity<GenericResponseDto<Object>> handleResendEmailNotFoundException(ResendEmailNotFoundException exception) {
        ResponseEntity<GenericResponseDto<Object>> response = new ResponseEntity<>(
                GenericResponseDto.builder()
                        .error(true)
                        .message(exception.getMessage())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
        return response;
    }

    @ExceptionHandler(InvalidOtpVerificationException.class)
    public ResponseEntity<GenericResponseDto<Object>> handleInvalidOtpVerificationException(InvalidOtpVerificationException exception) {
        ResponseEntity<GenericResponseDto<Object>> response = new ResponseEntity<>(
                GenericResponseDto.builder()
                        .error(true)
                        .message(exception.getMessage())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
        return response;
    }

    @ExceptionHandler(OtpAlreadyBeenUsedException.class)
    public ResponseEntity<GenericResponseDto<Object>> handleOtpAlreadyBeenUsedException(OtpAlreadyBeenUsedException exception) {
        ResponseEntity<GenericResponseDto<Object>> response = new ResponseEntity<>(
                GenericResponseDto.builder()
                        .error(true)
                        .message(exception.getMessage())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
        return response;
    }

    @ExceptionHandler(OtpExpiredException.class)
    public ResponseEntity<GenericResponseDto<Object>> handleOtpExpiredException(OtpExpiredException exception) {
        ResponseEntity<GenericResponseDto<Object>> response = new ResponseEntity<>(
                GenericResponseDto.builder()
                        .error(true)
                        .message(exception.getMessage())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
        return response;
    }

    @ExceptionHandler(WrongOtpException.class)
    public ResponseEntity<GenericResponseDto<Object>> handleWrongOtpException(WrongOtpException exception) {
        ResponseEntity<GenericResponseDto<Object>> response = new ResponseEntity<>(
                GenericResponseDto.builder()
                        .error(true)
                        .message(exception.getMessage())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
        return response;
    }

}
