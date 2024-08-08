package com.tinqinacademy.authenticationservice.api.operations.login;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tinqinacademy.authenticationservice.api.base.OperationOutput;
import lombok.*;

@Builder(toBuilder = true)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LoginOutput implements OperationOutput {
     @JsonIgnore
     private String jwt;
}
