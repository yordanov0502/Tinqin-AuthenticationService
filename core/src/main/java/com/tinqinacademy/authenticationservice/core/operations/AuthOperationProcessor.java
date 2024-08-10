package com.tinqinacademy.authenticationservice.core.operations;

import com.tinqinacademy.authenticationservice.api.exceptions.Errors;
import com.tinqinacademy.authenticationservice.api.operations.auth.AuthInput;
import com.tinqinacademy.authenticationservice.api.operations.auth.AuthOperation;
import com.tinqinacademy.authenticationservice.api.operations.auth.AuthOutput;
import com.tinqinacademy.authenticationservice.core.exceptions.ExceptionService;
import com.tinqinacademy.authenticationservice.core.security.JwtService;
import com.tinqinacademy.authenticationservice.core.utils.LoggingUtils;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthOperationProcessor extends BaseOperationProcessor implements AuthOperation {
    private final JwtService jwtService;
    public AuthOperationProcessor(ConversionService conversionService, ExceptionService exceptionService, Validator validator, JwtService jwtService) {
        super(conversionService, exceptionService, validator);
        this.jwtService = jwtService;
    }

    @Override
    public Either<Errors, AuthOutput> process(AuthInput input) {
        return Try.of(() -> {
            log.info(String.format("Start %s %s input: %s", this.getClass().getSimpleName(), LoggingUtils.getMethodName(), input));

            validate(input);

            Boolean isJWTValid = jwtService.validateJwt(input.getJwt());

            AuthOutput output = AuthOutput.builder()
                    .isJwtValid(isJWTValid)
                    .build();

            log.info(String.format("End %s %s output: %s", this.getClass().getSimpleName(), LoggingUtils.getMethodName(), output));

            return output;})
                .toEither()
                .mapLeft(exceptionService::handle);
    }


}
