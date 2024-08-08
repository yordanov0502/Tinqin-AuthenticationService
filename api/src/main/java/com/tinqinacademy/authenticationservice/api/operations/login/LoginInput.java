package com.tinqinacademy.authenticationservice.api.operations.login;

import com.tinqinacademy.authenticationservice.api.base.OperationInput;
import com.tinqinacademy.authenticationservice.api.validation.user.annotation.PasswordRegex;
import com.tinqinacademy.authenticationservice.api.validation.user.annotation.UsernameRegex;
import lombok.*;

@Builder(toBuilder = true)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LoginInput implements OperationInput {
    @UsernameRegex
    private String username;
    @PasswordRegex
    private String password;
}
