package com.tinqinacademy.authenticationservice.core.operations;

import com.tinqinacademy.authenticationservice.api.exceptions.Errors;
import com.tinqinacademy.authenticationservice.api.exceptions.custom.LoginException;
import com.tinqinacademy.authenticationservice.api.operations.login.LoginInput;
import com.tinqinacademy.authenticationservice.api.operations.login.LoginOperation;
import com.tinqinacademy.authenticationservice.api.operations.login.LoginOutput;
import com.tinqinacademy.authenticationservice.core.exceptions.ExceptionService;
import com.tinqinacademy.authenticationservice.core.security.JwtService;
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
public class LoginOperationProcessor extends BaseOperationProcessor implements LoginOperation {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    public LoginOperationProcessor(ConversionService conversionService, ExceptionService exceptionService, Validator validator, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        super(conversionService, exceptionService, validator);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public Either<Errors, LoginOutput> process(LoginInput input) {

        return Try.of(() -> {
            log.info(String.format("Start %s %s input: %s", this.getClass().getSimpleName(), LoggingUtils.getMethodName(), input));

            validate(input);
            User user = checkCredentials(input);
            String generatedJWT = jwtService.generateToken(user);
            LoginOutput output = LoginOutput.builder()
                    .jwt(generatedJWT)
                    .build();

            log.info(String.format("End %s %s output: %s", this.getClass().getSimpleName(), LoggingUtils.getMethodName(), output));

            return output;})
            .toEither()
            .mapLeft(exceptionService::handle);
    }

    public User checkCredentials(LoginInput input){
        log.info(String.format("Start %s %s input: %s", this.getClass().getSimpleName(),LoggingUtils.getMethodName(),input));

        User user = userRepository.findByUsername(input.getUsername())
                .orElseThrow(() -> new LoginException("Invalid username or password."));

        if(!passwordEncoder.matches(input.getPassword(),user.getPassword())){
            throw new LoginException("Invalid username or password.");
        }

        log.info(String.format("End %s %s output: %s", this.getClass().getSimpleName(),LoggingUtils.getMethodName(),input));

        return user;
    }


}