package com.tinqinacademy.authenticationservice.core.operations;

import com.tinqinacademy.authenticationservice.api.exceptions.Errors;
import com.tinqinacademy.authenticationservice.api.exceptions.custom.NotFoundException;
import com.tinqinacademy.authenticationservice.api.operations.resetpassword.ResetPasswordInput;
import com.tinqinacademy.authenticationservice.api.operations.resetpassword.ResetPasswordOperation;
import com.tinqinacademy.authenticationservice.api.operations.resetpassword.ResetPasswordOutput;
import com.tinqinacademy.authenticationservice.core.exceptions.ExceptionService;
import com.tinqinacademy.authenticationservice.core.utils.LoggingUtils;
import com.tinqinacademy.authenticationservice.core.utils.PasswordRecoveryCodesCacheSerivce;
import com.tinqinacademy.authenticationservice.persistence.model.entity.User;
import com.tinqinacademy.authenticationservice.persistence.repository.UserRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class ResetPasswordOperationProcessor extends BaseOperationProcessor implements ResetPasswordOperation{

    private final PasswordRecoveryCodesCacheSerivce passwordRecoveryCodesCacheSerivce;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ResetPasswordOperationProcessor(ConversionService conversionService, ExceptionService exceptionService, Validator validator, PasswordRecoveryCodesCacheSerivce passwordRecoveryCodesCacheSerivce, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        super(conversionService, exceptionService, validator);
        this.passwordRecoveryCodesCacheSerivce = passwordRecoveryCodesCacheSerivce;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Either<Errors, ResetPasswordOutput> process(ResetPasswordInput input) {
        return Try.of(() -> {
                    log.info(String.format("Start %s %s input: %s", this.getClass().getSimpleName(), LoggingUtils.getMethodName(), input));

                    validate(input);

                    String email = (String) passwordRecoveryCodesCacheSerivce.getKeyForValueInCache(input.getPasswordRecoveryCode());
                    User user = userRepository.findByEmail(email)
                            .orElseThrow(() -> new NotFoundException(String.format("User with email: %s doesn't exist.", email)));

                    String newHashedPassword = passwordEncoder.encode(input.getNewPassword());

                    User updatedUser = user.toBuilder()
                            .password(newHashedPassword)
                            .build();
                    userRepository.save(updatedUser);

                    passwordRecoveryCodesCacheSerivce.removeFromCache(email);

                    ResetPasswordOutput output = ResetPasswordOutput.builder().build();
                    log.info(String.format("End %s %s output: %s", this.getClass().getSimpleName(), LoggingUtils.getMethodName(), output));

                    return output;
                })
                .toEither()
                .mapLeft(exceptionService::handle);
    }
}