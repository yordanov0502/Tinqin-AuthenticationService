package com.tinqinacademy.authenticationservice.rest.controllers;

import com.tinqinacademy.authenticationservice.api.base.OperationOutput;
import com.tinqinacademy.authenticationservice.api.exceptions.Errors;
import com.tinqinacademy.authenticationservice.api.operations.login.LoginOutput;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
public abstract class BaseController {

    protected <O extends OperationOutput> ResponseEntity<?> mapToResponseEntity(Either<Errors, O> either, HttpStatus httpStatus) {

        return either.isRight()
                ? new ResponseEntity<>(either.get(), httpStatus)
                : new ResponseEntity<>(either.getLeft().getErrorList(), either.getLeft().getHttpStatus());
    }

    protected ResponseEntity<?> mapToResponseEntityWithJWT(Either<Errors, LoginOutput> either, HttpStatus httpStatus) {

        if(either.isRight()){
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.AUTHORIZATION,"Bearer "+either.get().getJwt());
            return new ResponseEntity<>(either.get(),headers,httpStatus);
        }
        else{
            return new ResponseEntity<>(either.getLeft().getErrorList(), either.getLeft().getHttpStatus());
        }
    }

}