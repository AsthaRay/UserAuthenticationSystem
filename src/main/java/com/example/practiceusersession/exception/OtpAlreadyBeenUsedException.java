package com.example.practiceusersession.exception;

public class OtpAlreadyBeenUsedException extends RuntimeException {
    public OtpAlreadyBeenUsedException(String message) {
        super(message);
    }
}
