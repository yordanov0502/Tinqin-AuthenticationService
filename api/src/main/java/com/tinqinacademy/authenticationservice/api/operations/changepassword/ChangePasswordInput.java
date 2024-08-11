package com.tinqinacademy.authenticationservice.api.operations.changepassword;

import com.tinqinacademy.authenticationservice.api.base.OperationInput;
import com.tinqinacademy.authenticationservice.api.validation.user.password.PasswordRegex;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder(toBuilder = true)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordInput implements OperationInput {
    
    @PasswordRegex
    private String oldPassword;
    @PasswordRegex
    private String newPassword;
    @NotBlank
    @Email
    private String email;
}
