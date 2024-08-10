package com.tinqinacademy.authenticationservice.api.operations.auth;

import com.tinqinacademy.authenticationservice.api.base.OperationInput;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder(toBuilder = true)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AuthInput implements OperationInput {
    @NotBlank
    private String jwt;
}
