package com.tinqinacademy.authenticationservice.api.operations.logout;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tinqinacademy.authenticationservice.api.base.OperationInput;
import lombok.*;

@Builder(toBuilder = true)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LogoutInput implements OperationInput {
    @JsonIgnore
    private String jwt;
    @JsonIgnore
    private String userContextId;
}
