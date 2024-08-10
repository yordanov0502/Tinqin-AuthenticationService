package com.tinqinacademy.authenticationservice.api.exceptions.custom;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AccountConfirmationException extends CustomException{
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public AccountConfirmationException(String message) {super(message);}
}
