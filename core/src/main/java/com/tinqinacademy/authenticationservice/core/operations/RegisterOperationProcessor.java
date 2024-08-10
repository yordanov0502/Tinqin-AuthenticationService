package com.tinqinacademy.authenticationservice.core.operations;

import com.tinqinacademy.authenticationservice.api.exceptions.Errors;
import com.tinqinacademy.authenticationservice.api.exceptions.custom.DuplicateValueException;
import com.tinqinacademy.authenticationservice.api.operations.register.RegisterInput;
import com.tinqinacademy.authenticationservice.api.operations.register.RegisterOperation;
import com.tinqinacademy.authenticationservice.api.operations.register.RegisterOutput;
import com.tinqinacademy.authenticationservice.core.exceptions.ExceptionService;
import com.tinqinacademy.authenticationservice.core.utils.CodeGenerator;
import com.tinqinacademy.authenticationservice.core.utils.EmailService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class RegisterOperationProcessor extends BaseOperationProcessor implements RegisterOperation {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AccountCodeRepository accountCodeRepository;
    private final EmailService emailService;
    private final CodeGenerator codeGenerator;

    public RegisterOperationProcessor(ConversionService conversionService, ExceptionService exceptionService, Validator validator, PasswordEncoder passwordEncoder, UserRepository userRepository, AccountCodeRepository accountCodeRepository, EmailService emailService, CodeGenerator codeGenerator) {
        super(conversionService, exceptionService, validator);
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.accountCodeRepository = accountCodeRepository;
        this.emailService = emailService;
        this.codeGenerator = codeGenerator;
    }

    @Transactional
    @Override
    public Either<Errors, RegisterOutput> process(RegisterInput input) {

        return Try.of(() -> {
                    log.info(String.format("Start %s %s input: %s", this.getClass().getSimpleName(), LoggingUtils.getMethodName(), input));

                    validate(input);
                    checkForExistingUsernameAndEmail(input.getUsername(), input.getEmail());

                    User user = conversionService.convert(input, User.UserBuilder.class)
                            .password(passwordEncoder.encode(input.getPassword()))
                            .build();
                    User newUser = userRepository.save(user);

                    String generatedCode = codeGenerator.generateRandomCode();
                    AccountCode accountConfirmationCode = AccountCode.builder()
                            .code(generatedCode)
                            .email(newUser.getEmail())
                            .build();
                    accountCodeRepository.save(accountConfirmationCode);
                    emailService.sendEmailForAccountActivation(newUser.getFirstName(), newUser.getEmail(), generatedCode);

                    RegisterOutput output = RegisterOutput.builder()
                            .id(newUser.getId().toString())
                            .build();

                    log.info(String.format("End %s %s output: %s", this.getClass().getSimpleName(), LoggingUtils.getMethodName(), output));

                    return output;
                })
                .toEither()
                .mapLeft(exceptionService::handle);
    }

    private void checkForExistingUsername(String username) {
        log.info(String.format("Start %s %s input: %s", this.getClass().getSimpleName(), LoggingUtils.getMethodName(), username));

        boolean isUsernameExists = userRepository.existsByUsername(username);
        if (isUsernameExists) {
            throw new DuplicateValueException(String.format("Username: %s already exists.", username));
        }

        log.info(String.format("End %s %s.", this.getClass().getSimpleName(), LoggingUtils.getMethodName()));
    }

    private void checkForExistingEmail(String email) {
        log.info(String.format("Start %s %s input: %s", this.getClass().getSimpleName(), LoggingUtils.getMethodName(), email));

        boolean isEmailExists = userRepository.existsByEmail(email);
        if (isEmailExists) {
            throw new DuplicateValueException(String.format("Email: %s already exists.", email));
        }

        log.info(String.format("End %s %s.", this.getClass().getSimpleName(), LoggingUtils.getMethodName()));
    }

    private void checkForExistingUsernameAndEmail(String username, String email) {
        log.info(String.format("Start %s %s input: %s,%s", this.getClass().getSimpleName(), LoggingUtils.getMethodName(), username, email));
        checkForExistingUsername(username);
        checkForExistingEmail(email);
        log.info(String.format("End %s %s.", this.getClass().getSimpleName(), LoggingUtils.getMethodName()));
    }

}
