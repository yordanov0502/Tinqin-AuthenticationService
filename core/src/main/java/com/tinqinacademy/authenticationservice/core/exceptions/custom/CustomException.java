package com.tinqinacademy.authenticationservice.core.exceptions.custom;

import org.springframework.http.HttpStatus;

public abstract class CustomException extends RuntimeException{

    public CustomException(String message) {super(message);}

    public abstract HttpStatus getHttpStatus();
}
