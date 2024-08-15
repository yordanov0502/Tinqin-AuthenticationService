package com.tinqinacademy.authenticationservice.core.operations;

import com.tinqinacademy.authenticationservice.api.exceptions.Errors;
import com.tinqinacademy.authenticationservice.api.exceptions.custom.NotFoundException;
import com.tinqinacademy.authenticationservice.api.exceptions.custom.PromotionException;
import com.tinqinacademy.authenticationservice.api.operations.promote.PromoteInput;
import com.tinqinacademy.authenticationservice.api.operations.promote.PromoteOperation;
import com.tinqinacademy.authenticationservice.api.operations.promote.PromoteOutput;
import com.tinqinacademy.authenticationservice.core.exceptions.ExceptionService;
import com.tinqinacademy.authenticationservice.core.utils.LoggingUtils;
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
public class PromoteOperationProcessor extends BaseOperationProcessor implements PromoteOperation {

    private final UserRepository userRepository;

    public PromoteOperationProcessor(ConversionService conversionService, ExceptionService exceptionService, Validator validator, UserRepository userRepository) {
        super(conversionService, exceptionService, validator);
        this.userRepository = userRepository;
    }

    @Override
    public Either<Errors, PromoteOutput> process(PromoteInput input) {
        return Try.of(() -> {
                    log.info(String.format("Start %s %s input: %s", this.getClass().getSimpleName(), LoggingUtils.getMethodName(), input));

                    validate(input);
                    User user = checkUserExistenceAndIdsAndRole(input);
                    User userForPromotion = user.toBuilder()
                            .role(Role.ADMIN)
                            .build();
                    userRepository.save(userForPromotion);

                    PromoteOutput output = PromoteOutput.builder().build();
                    log.info(String.format("End %s %s output: %s", this.getClass().getSimpleName(), LoggingUtils.getMethodName(), output));
                    return output;
                })
                .toEither()
                .mapLeft(exceptionService::handle);
    }

    private User checkUserExistenceAndIdsAndRole(PromoteInput input) {
        log.info(String.format("Start %s %s input: %s", this.getClass().getSimpleName(), LoggingUtils.getMethodName(), input));

        User userForPromotion = userRepository.findById(UUID.fromString(input.getUserId()))
                .orElseThrow(() -> new NotFoundException(String.format("User with id: %s doesn't exist", input.getUserId())));

        if (userForPromotion.getId().equals(input.getUserContextId())) {
            throw new PromotionException("You cannot promote yourself to admin, because you are admin.");
        }

        if (userForPromotion.getRole().toString().equals(Role.ADMIN.toString())) {
            throw new PromotionException(String.format("User with id: %s is already an admin.", userForPromotion.getId()));
        }

        log.info(String.format("End %s %s output: %s", this.getClass().getSimpleName(), LoggingUtils.getMethodName(), userForPromotion));
        return userForPromotion;
    }
}
