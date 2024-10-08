package com.tinqinacademy.authenticationservice.core.operations;

import com.tinqinacademy.authenticationservice.api.exceptions.Errors;
import com.tinqinacademy.authenticationservice.api.operations.logout.LogoutInput;
import com.tinqinacademy.authenticationservice.api.operations.logout.LogoutOperation;
import com.tinqinacademy.authenticationservice.api.operations.logout.LogoutOutput;
import com.tinqinacademy.authenticationservice.core.exceptions.ExceptionService;
import com.tinqinacademy.authenticationservice.core.utils.JwtBlacklistCacheService;
import com.tinqinacademy.authenticationservice.core.utils.LoggingUtils;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogoutOperationProcessor extends BaseOperationProcessor implements LogoutOperation {

    private final JwtBlacklistCacheService jwtBlacklist;

    public LogoutOperationProcessor(ConversionService conversionService, ExceptionService exceptionService, Validator validator, JwtBlacklistCacheService jwtBlacklist) {
        super(conversionService, exceptionService, validator);
        this.jwtBlacklist = jwtBlacklist;
    }

    @Override
    public Either<Errors, LogoutOutput> process(LogoutInput input) {
        return Try.of(() -> {
                    log.info(String.format("Start %s %s input: %s", this.getClass().getSimpleName(), LoggingUtils.getMethodName(), input));
                    validate(input);

                    jwtBlacklist.addToCache(input.getJwt(), input.getUserContextId());
                    LogoutOutput output = LogoutOutput.builder().build();

                    log.info(String.format("End %s %s output: %s", this.getClass().getSimpleName(), LoggingUtils.getMethodName(), output));
                    return output;
                })
                .toEither()
                .mapLeft(exceptionService::handle);
    }
}
