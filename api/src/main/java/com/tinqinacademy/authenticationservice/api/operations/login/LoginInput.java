package com.tinqinacademy.authenticationservice.api.operations.login;

import com.tinqinacademy.authenticationservice.api.base.OperationInput;
import com.tinqinacademy.authenticationservice.api.validation.user.password.PasswordRegex;
import com.tinqinacademy.authenticationservice.api.validation.user.username.UsernameRegex;
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
