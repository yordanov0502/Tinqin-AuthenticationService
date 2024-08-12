package com.tinqinacademy.authenticationservice.core.operations;

import com.tinqinacademy.authenticationservice.api.exceptions.Errors;
import com.tinqinacademy.authenticationservice.api.exceptions.custom.DemotionException;
import com.tinqinacademy.authenticationservice.api.exceptions.custom.NotFoundException;
import com.tinqinacademy.authenticationservice.api.operations.demote.DemoteInput;
import com.tinqinacademy.authenticationservice.api.operations.demote.DemoteOperation;
import com.tinqinacademy.authenticationservice.api.operations.demote.DemoteOutput;
import com.tinqinacademy.authenticationservice.core.exceptions.ExceptionService;
import com.tinqinacademy.authenticationservice.core.utils.LoggingUtils;
import com.tinqinacademy.authenticationservice.persistence.model.context.UserContext;
import com.tinqinacademy.authenticationservice.persistence.model.entity.User;
import com.tinqinacademy.authenticationservice.persistence.model.enums.Role;
import com.tinqinacademy.authenticationservice.persistence.repository.UserRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class DemoteOperationProcessor extends BaseOperationProcessor implements DemoteOperation {

    private final UserContext userContext;
    private final UserRepository userRepository;

    public DemoteOperationProcessor(ConversionService conversionService, ExceptionService exceptionService, Validator validator, UserContext userContext, UserRepository userRepository) {
        super(conversionService, exceptionService, validator);
        this.userContext = userContext;
        this.userRepository = userRepository;
    }

    @Override
    public Either<Errors, DemoteOutput> process(DemoteInput input) {
        return Try.of(() -> {
                    log.info(String.format("Start %s %s input: %s", this.getClass().getSimpleName(), LoggingUtils.getMethodName(), input));

                    validate(input);
                    User user = performRoleValidation(input);
                    User userForDemotion = user.toBuilder()
                            .role(Role.USER)
                            .build();
                    userRepository.save(userForDemotion);

                    DemoteOutput output = DemoteOutput.builder().build();
                    log.info(String.format("End %s %s output: %s", this.getClass().getSimpleName(), LoggingUtils.getMethodName(), output));
                    return output;
                })
                .toEither()
                .mapLeft(exceptionService::handle);
    }

    private User performRoleValidation(DemoteInput input){
        log.info(String.format("Start %s %s input: %s", this.getClass().getSimpleName(), LoggingUtils.getMethodName(), input));

        User userForDemotion = userRepository.findById(UUID.fromString(input.getUserId()))
                .orElseThrow(() -> new NotFoundException(String.format("User with id: %s doesn't exist.", input.getUserId())));

        if (userForDemotion.getId().equals(userContext.getCurrAuthorizedUser().getId())) {
            throw new DemotionException("You cannot demote yourself.");
        }

        if (userForDemotion.getRole().toString().equals(Role.USER.toString())) {
            throw new DemotionException(String.format("User with id: %s has already a USER role.", userForDemotion.getId()));
        }

        log.info(String.format("End %s %s output: %s", this.getClass().getSimpleName(), LoggingUtils.getMethodName(), userForDemotion));
        return userForDemotion;
    }
}
