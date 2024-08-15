package com.tinqinacademy.authenticationservice.core.operations;

import com.tinqinacademy.authenticationservice.api.exceptions.Errors;
import com.tinqinacademy.authenticationservice.api.exceptions.custom.PasswordException;
import com.tinqinacademy.authenticationservice.api.operations.changepassword.ChangePasswordInput;
import com.tinqinacademy.authenticationservice.api.operations.changepassword.ChangePasswordOperation;
import com.tinqinacademy.authenticationservice.api.operations.changepassword.ChangePasswordOutput;
import com.tinqinacademy.authenticationservice.core.exceptions.ExceptionService;
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

@Slf4j
@Service
public class ChangePasswordOperationProcessor extends BaseOperationProcessor implements ChangePasswordOperation {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public ChangePasswordOperationProcessor(ConversionService conversionService, ExceptionService exceptionService, Validator validator, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        super(conversionService, exceptionService, validator);
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public Either<Errors, ChangePasswordOutput> process(ChangePasswordInput input) {
        return Try.of(() -> {
                    log.info(String.format("Start %s %s input: %s", this.getClass().getSimpleName(), LoggingUtils.getMethodName(), input));

                    validate(input);
                    validatePasswords(input);

                    User currUser = userRepository.findByEmail(input.getUserContextEmail()).get(); //? User context is checked and set in access interceptor, so NO exception will ever occur here
                    String newPassword = passwordEncoder.encode(input.getNewPassword());
                    currUser.setPassword(newPassword);
                    userRepository.save(currUser);

                    ChangePasswordOutput output = ChangePasswordOutput.builder().build();
                    log.info(String.format("End %s %s output: %s", this.getClass().getSimpleName(), LoggingUtils.getMethodName(), output));

                    return output;})
                .toEither()
                .mapLeft(exceptionService::handle);
    }

    private void validatePasswords(ChangePasswordInput input) {
        log.info(String.format("Start %s %s input: %s", this.getClass().getSimpleName(), LoggingUtils.getMethodName(), input));

        if(input.getNewPassword().equals(input.getOldPassword())){
            throw new PasswordException("New password must be different than the old password.");
        }

        if(!passwordEncoder.matches(input.getOldPassword(),input.getUserContextOldPassword())){
            throw new PasswordException("Wrong old password.");
        }

        if(!input.getEmail().equals(input.getUserContextEmail())){
            throw new PasswordException("Wrong email.");
        }

        log.info(String.format("End %s %s.", this.getClass().getSimpleName(), LoggingUtils.getMethodName()));
    }
}
