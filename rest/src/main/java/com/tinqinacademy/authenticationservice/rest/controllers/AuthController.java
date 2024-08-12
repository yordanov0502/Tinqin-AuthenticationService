package com.tinqinacademy.authenticationservice.rest.controllers;

import com.tinqinacademy.authenticationservice.api.RestApiRoutes;
import com.tinqinacademy.authenticationservice.api.exceptions.Errors;
import com.tinqinacademy.authenticationservice.api.operations.changepassword.ChangePasswordInput;
import com.tinqinacademy.authenticationservice.api.operations.changepassword.ChangePasswordOperation;
import com.tinqinacademy.authenticationservice.api.operations.changepassword.ChangePasswordOutput;
import com.tinqinacademy.authenticationservice.api.operations.confirmregistration.ConfirmRegistrationInput;
import com.tinqinacademy.authenticationservice.api.operations.confirmregistration.ConfirmRegistrationOperation;
import com.tinqinacademy.authenticationservice.api.operations.confirmregistration.ConfirmRegistrationOutput;
import com.tinqinacademy.authenticationservice.api.operations.demote.DemoteInput;
import com.tinqinacademy.authenticationservice.api.operations.demote.DemoteOperation;
import com.tinqinacademy.authenticationservice.api.operations.demote.DemoteOutput;
import com.tinqinacademy.authenticationservice.api.operations.login.LoginInput;
import com.tinqinacademy.authenticationservice.api.operations.login.LoginOperation;
import com.tinqinacademy.authenticationservice.api.operations.login.LoginOutput;
import com.tinqinacademy.authenticationservice.api.operations.logout.LogoutInput;
import com.tinqinacademy.authenticationservice.api.operations.logout.LogoutOperation;
import com.tinqinacademy.authenticationservice.api.operations.logout.LogoutOutput;
import com.tinqinacademy.authenticationservice.api.operations.promote.PromoteInput;
import com.tinqinacademy.authenticationservice.api.operations.promote.PromoteOperation;
import com.tinqinacademy.authenticationservice.api.operations.promote.PromoteOutput;
import com.tinqinacademy.authenticationservice.api.operations.recoverpassword.RecoverPasswordInput;
import com.tinqinacademy.authenticationservice.api.operations.recoverpassword.RecoverPasswordOperation;
import com.tinqinacademy.authenticationservice.api.operations.recoverpassword.RecoverPasswordOutput;
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
    private final RecoverPasswordOperation recoverPasswordOperation;
    private final ConfirmRegistrationOperation confirmRegistrationOperation;
    private final ChangePasswordOperation changePasswordOperation;
    private final PromoteOperation promoteOperation;
    private final DemoteOperation demoteOperation;
    private final LogoutOperation logoutOperation;

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
    public ResponseEntity<?> register(@RequestBody RegisterInput input) {
        Either<Errors,RegisterOutput> either = registerOperation.process(input);
        return mapToResponseEntity(either,HttpStatus.CREATED);
    }

    @Operation(summary = "Recover password.",
            description = "Always gives 200 code. Sends email with new random generated password only if the email is registered to a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New random generated password has been successfully processed."),
            @ApiResponse(responseCode = "400", description = "Bad request."),
            @ApiResponse(responseCode = "404", description = "Not found.")
    })
    @PostMapping(RestApiRoutes.RECOVER_PASSWORD)
    public ResponseEntity<?> recoverPassword(@RequestBody RecoverPasswordInput input) {
        Either<Errors, RecoverPasswordOutput> either = recoverPasswordOperation.process(input);
        return mapToResponseEntity(either,HttpStatus.OK);
    }

    @Operation(summary = "Confirm registration.",
            description = "Activates user account and allows for login to complete successfully.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User account has been activated successfully."),
            @ApiResponse(responseCode = "400", description = "Bad request."),
            @ApiResponse(responseCode = "404", description = "Not found.")
    })
    @PostMapping(RestApiRoutes.CONFIRM_REGISTRATION)
    public ResponseEntity<?> confirmRegistration(@RequestBody ConfirmRegistrationInput input) {
        Either<Errors, ConfirmRegistrationOutput> either = confirmRegistrationOperation.process(input);
        return mapToResponseEntity(either,HttpStatus.OK);
    }

    @Operation(summary = "Change password.",
            description = "Changes the user password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User password has been changed successfully."),
            @ApiResponse(responseCode = "400", description = "Bad request."),
            @ApiResponse(responseCode = "401", description = "Unauthorized."),
            @ApiResponse(responseCode = "404", description = "Not found.")
    })
    @PostMapping(RestApiRoutes.CHANGE_PASSWORD)
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordInput input) {
        Either<Errors, ChangePasswordOutput> either = changePasswordOperation.process(input);
        return mapToResponseEntity(either, HttpStatus.OK);
    }

    @Operation(summary = "Promote user.",
            description = "Promotes user to admin and give admin rights. Only admin can promote another user to admin.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User has been successfully promoted to admin."),
            @ApiResponse(responseCode = "400", description = "Bad request."),
            @ApiResponse(responseCode = "401", description = "Unauthorized."),
            @ApiResponse(responseCode = "403", description = "Forbidden."),
            @ApiResponse(responseCode = "404", description = "Not found.")
    })
    @PostMapping(RestApiRoutes.PROMOTE)
    public ResponseEntity<?> promoteUserToAdmin(@RequestBody PromoteInput input) {
        Either<Errors, PromoteOutput> either = promoteOperation.process(input);
        return mapToResponseEntity(either, HttpStatus.OK);
    }

    @Operation(summary = "Demote admin.",
            description = "Removes admin privileges of any user. System must always have at least 1 admin. Admin cannot demote theirself.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Admin has been successfully demoted to user."),
            @ApiResponse(responseCode = "400", description = "Bad request."),
            @ApiResponse(responseCode = "401", description = "Unauthorized."),
            @ApiResponse(responseCode = "403", description = "Forbidden."),
            @ApiResponse(responseCode = "404", description = "Not found.")
    })
    @PostMapping(RestApiRoutes.DEMOTE)
    public ResponseEntity<?> demoteAdminToUser(@RequestBody DemoteInput input) {
        Either<Errors, DemoteOutput> either = demoteOperation.process(input);
        return mapToResponseEntity(either, HttpStatus.OK);
    }

    @Operation(summary = "Logout.",
            description = "Logouts the user and adds his JWT to a blacklist.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User has logged out successfully."),
            @ApiResponse(responseCode = "400", description = "Bad request."),
            @ApiResponse(responseCode = "401", description = "Unauthorized."),
            @ApiResponse(responseCode = "404", description = "Not found.")
    })
    @PostMapping(RestApiRoutes.LOGOUT)
    public ResponseEntity<?> logout() {
        LogoutInput input = LogoutInput.builder().build();
        Either<Errors, LogoutOutput> either = logoutOperation.process(input);
        return mapToResponseEntity(either,HttpStatus.OK);
    }


}