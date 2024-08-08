package com.tinqinacademy.authenticationservice.api.exceptions.custom;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class LoginException extends CustomException{

    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public LoginException(String message) {super(message);}
}
