package com.tinqinacademy.authenticationservice.core.operations;

import com.tinqinacademy.authenticationservice.api.exceptions.Errors;
import com.tinqinacademy.authenticationservice.api.exceptions.custom.AccountConfirmationException;
import com.tinqinacademy.authenticationservice.api.exceptions.custom.NotFoundException;
import com.tinqinacademy.authenticationservice.api.operations.confirmregistration.ConfirmRegistrationInput;
import com.tinqinacademy.authenticationservice.api.operations.confirmregistration.ConfirmRegistrationOperation;
import com.tinqinacademy.authenticationservice.api.operations.confirmregistration.ConfirmRegistrationOutput;
import com.tinqinacademy.authenticationservice.core.exceptions.ExceptionService;
import com.tinqinacademy.authenticationservice.core.utils.LoggingUtils;
import com.tinqinacademy.authenticationservice.persistence.model.entity.AccountCode;
import com.tinqinacademy.authenticationservice.persistence.model.entity.User;
import com.tinqinacademy.authenticationservice.persistence.repository.AccountCodeRepository;
import com.tinqinacademy.authenticationservice.persistence.repository.UserRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ConfirmRegistrationOperationProcessor extends BaseOperationProcessor implements ConfirmRegistrationOperation {

    private final UserRepository userRepository;
    private final AccountCodeRepository accountCodeRepository;

    public ConfirmRegistrationOperationProcessor(ConversionService conversionService, ExceptionService exceptionService, Validator validator, UserRepository userRepository, AccountCodeRepository accountCodeRepository) {
        super(conversionService, exceptionService, validator);
        this.userRepository = userRepository;
        this.accountCodeRepository = accountCodeRepository;
    }

    @Transactional
    @Override
    public Either<Errors, ConfirmRegistrationOutput> process(ConfirmRegistrationInput input) {
        return Try.of(() -> {
                    log.info(String.format("Start %s %s input: %s", this.getClass().getSimpleName(), LoggingUtils.getMethodName(), input));

                    validate(input);
                    User user = checkAccountConfirmation(input.getConfirmationCode());

                    accountCodeRepository.deleteByEmail(user.getEmail());
                    user.setIsConfirmed(true);
                    userRepository.save(user);
                    ConfirmRegistrationOutput output = ConfirmRegistrationOutput.builder().build();

                    log.info(String.format("End %s %s output: %s", this.getClass().getSimpleName(), LoggingUtils.getMethodName(), output));
                    return output;
                })
                .toEither()
                .mapLeft(exceptionService::handle);
    }

    public User checkAccountConfirmation(String confirmationCode) {
        log.info(String.format("Start %s %s input: %s", this.getClass().getSimpleName(), LoggingUtils.getMethodName(), confirmationCode));

        AccountCode accountCode = accountCodeRepository.findByCode(confirmationCode)
                .orElseThrow(() -> new AccountConfirmationException("Invalid code."));

        User user = userRepository.findByEmail(accountCode.getEmail())
                .orElseThrow(() -> new NotFoundException(String.format("User with email: %s doesn't exist.", accountCode.getEmail())));

        if (user.getIsConfirmed()) {throw new AccountConfirmationException("Account is already confirmed.");}

        log.info(String.format("End %s %s output: %s", this.getClass().getSimpleName(), LoggingUtils.getMethodName(), user));
        return user;
    }

}