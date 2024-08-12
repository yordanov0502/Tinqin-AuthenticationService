package com.tinqinacademy.authenticationservice.core.operations;

import com.tinqinacademy.authenticationservice.api.exceptions.Errors;
import com.tinqinacademy.authenticationservice.api.operations.recoverpassword.RecoverPasswordInput;
import com.tinqinacademy.authenticationservice.api.operations.recoverpassword.RecoverPasswordOperation;
import com.tinqinacademy.authenticationservice.api.operations.recoverpassword.RecoverPasswordOutput;
import com.tinqinacademy.authenticationservice.core.exceptions.ExceptionService;
import com.tinqinacademy.authenticationservice.core.utils.ContentGenerator;
import com.tinqinacademy.authenticationservice.core.utils.EmailService;
import com.tinqinacademy.authenticationservice.core.utils.LoggingUtils;
import com.tinqinacademy.authenticationservice.persistence.model.entity.User;
import com.tinqinacademy.authenticationservice.persistence.repository.UserRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class RecoverPasswordOperationProcessor extends BaseOperationProcessor implements RecoverPasswordOperation {

    private final UserRepository userRepository;
    private final ContentGenerator contentGenerator;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public RecoverPasswordOperationProcessor(ConversionService conversionService, ExceptionService exceptionService, Validator validator, UserRepository userRepository, ContentGenerator contentGenerator, PasswordEncoder passwordEncoder, EmailService emailService) {
        super(conversionService, exceptionService, validator);
        this.userRepository = userRepository;
        this.contentGenerator = contentGenerator;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    @Override
    public Either<Errors, RecoverPasswordOutput> process(RecoverPasswordInput input) {
        return Try.of(() -> {
                    log.info(String.format("Start %s %s input: %s", this.getClass().getSimpleName(), LoggingUtils.getMethodName(), input));

                    validate(input);
                    Optional<User> user = checkForExistingEmail(input.getEmail());

                    if (user.isPresent()) {

                        String newRandomGeneratedPassword = contentGenerator.generateRandomPassword();
                        String newHashedPassword = passwordEncoder.encode(newRandomGeneratedPassword);

                        User userWithNewPass = user.get().toBuilder()
                                .password(newHashedPassword)
                                .build();
                        userRepository.save(userWithNewPass);

                        emailService.sendEmailWithNewPassword(
                                userWithNewPass.getFirstName(),
                                userWithNewPass.getEmail(),
                                newRandomGeneratedPassword);
                    }

                    RecoverPasswordOutput output = RecoverPasswordOutput.builder().build();
                    log.info(String.format("End %s %s output: %s", this.getClass().getSimpleName(), LoggingUtils.getMethodName(), output));

                    return output;
                })
                .toEither()
                .mapLeft(exceptionService::handle);
    }

    private Optional<User> checkForExistingEmail(String email) {
        log.info(String.format("Start %s %s input: %s", this.getClass().getSimpleName(), LoggingUtils.getMethodName(), email));

        Optional<User> user = userRepository.findByEmail(email);

        log.info(String.format("End %s %s. output: %s", this.getClass().getSimpleName(), LoggingUtils.getMethodName(), user));
        return user;
    }
}
