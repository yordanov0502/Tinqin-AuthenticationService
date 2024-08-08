package com.tinqinacademy.authenticationservice.api.operations.register;

import com.tinqinacademy.authenticationservice.api.base.OperationOutput;
import lombok.*;

@Builder(toBuilder = true)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RegisterOutput implements OperationOutput {
    private String id;
}
