package com.tinqinacademy.authenticationservice.core.exceptions;

import com.tinqinacademy.authenticationservice.api.error.Errors;

public interface ExceptionService {
    Errors handle(Throwable throwable);
}
