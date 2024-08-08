package com.tinqinacademy.authenticationservice.rest.controllers;

import com.tinqinacademy.authenticationservice.api.RestApiRoutes;
import com.tinqinacademy.authenticationservice.api.exceptions.Errors;
import com.tinqinacademy.authenticationservice.api.operations.login.LoginInput;
import com.tinqinacademy.authenticationservice.api.operations.login.LoginOperation;
import com.tinqinacademy.authenticationservice.api.operations.login.LoginOutput;
import com.tinqinacademy.authenticationservice.api.operations.register.RegisterInput;
import com.tinqinacademy.authenticationservice.api.operations.register.RegisterOperation;
import com.tinqinacademy.authenticationservice.api.operations.register.RegisterOutput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController extends BaseController{

    private final LoginOperation loginOperation;
    private final RegisterOperation registerOperation;

    @Operation(summary = "Login.",
            description = "Logins the user and issues a JWT with 5 min validity.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User has logged in successfully."),
            @ApiResponse(responseCode = "400", description = "Bad request."),
            @ApiResponse(responseCode = "404", description = "Not found.")
    })
    @PostMapping(RestApiRoutes.LOGIN)
    public ResponseEntity<?> login(@RequestBody LoginInput input) {
        Either<Errors, LoginOutput> either = loginOperation.process(input);
        return mapToResponseEntityWithJWT(either,HttpStatus.OK);
    }

    @Operation(summary = "Register.",
            description = "User receives a confirmation email once a registration. BONUS: make sending a one time code to confirm email. User cannot login until email is confirmed.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User executed successful registration."),
            @ApiResponse(responseCode = "400", description = "Bad request."),
            @ApiResponse(responseCode = "404", description = "Not found.")
    })
    @PostMapping(RestApiRoutes.REGISTER)
    public ResponseEntity<?> login(@RequestBody RegisterInput input) {
        //TODO: Implement sending code on email
        Either<Errors,RegisterOutput> either = registerOperation.process(input);
        return mapToResponseEntity(either,HttpStatus.CREATED);
    }
}
