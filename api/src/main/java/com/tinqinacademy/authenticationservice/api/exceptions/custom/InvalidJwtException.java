package com.tinqinacademy.authenticationservice.api.exceptions.custom;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidJwtException extends CustomException{

    private final HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

    public InvalidJwtException(String message) {super(message);}
}