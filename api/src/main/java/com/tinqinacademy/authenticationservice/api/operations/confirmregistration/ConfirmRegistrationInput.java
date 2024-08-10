package com.tinqinacademy.authenticationservice.api.operations.confirmregistration;

import com.tinqinacademy.authenticationservice.api.base.OperationInput;
import com.tinqinacademy.authenticationservice.api.validation.accountcode.AccountConfirmationCodeRegex;
import lombok.*;

@Builder(toBuilder = true)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmRegistrationInput implements OperationInput {
    @AccountConfirmationCodeRegex
    private String confirmationCode;
}
