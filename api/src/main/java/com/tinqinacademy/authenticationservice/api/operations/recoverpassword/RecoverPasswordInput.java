package com.tinqinacademy.authenticationservice.api.operations.recoverpassword;

import com.tinqinacademy.authenticationservice.api.base.OperationInput;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder(toBuilder = true)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RecoverPasswordInput implements OperationInput {
    @NotBlank
    @Email(message = "Invalid type of email.")
    private String email;
}
