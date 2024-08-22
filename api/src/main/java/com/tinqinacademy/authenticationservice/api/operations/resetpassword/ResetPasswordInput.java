package com.tinqinacademy.authenticationservice.api.operations.resetpassword;

import com.tinqinacademy.authenticationservice.api.base.OperationInput;
import com.tinqinacademy.authenticationservice.api.validation.user.password.PasswordRegex;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder(toBuilder = true)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordInput implements OperationInput {
    @NotBlank
    private String passwordRecoveryCode;
    @PasswordRegex
    private String newPassword;
}
