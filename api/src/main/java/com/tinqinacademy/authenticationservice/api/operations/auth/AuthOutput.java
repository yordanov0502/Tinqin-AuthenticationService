package com.tinqinacademy.authenticationservice.api.operations.auth;

import com.tinqinacademy.authenticationservice.api.base.OperationOutput;
import lombok.*;

@Builder(toBuilder = true)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AuthOutput implements OperationOutput {
    private Boolean isJwtValid;
}
