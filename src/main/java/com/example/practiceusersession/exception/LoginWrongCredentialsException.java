package com.example.practiceusersession.exception;

public class LoginWrongCredentialsException extends RuntimeException {
  public LoginWrongCredentialsException(String message) {
    super(message);
  }
}
