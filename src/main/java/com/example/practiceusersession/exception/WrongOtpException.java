package com.example.practiceusersession.exception;

public class WrongOtpException extends RuntimeException {
    public WrongOtpException(String message) {
        super(message);
    }
}
