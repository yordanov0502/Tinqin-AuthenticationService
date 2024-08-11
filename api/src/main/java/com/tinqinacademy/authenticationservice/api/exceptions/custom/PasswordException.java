package com.tinqinacademy.authenticationservice.api.exceptions.custom;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PasswordException extends CustomException{

    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public PasswordException(String message) {super(message);}
}