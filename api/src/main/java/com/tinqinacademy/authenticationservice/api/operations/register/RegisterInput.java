package com.tinqinacademy.authenticationservice.api.operations.register;

import com.tinqinacademy.authenticationservice.api.base.OperationInput;
import com.tinqinacademy.authenticationservice.api.validation.user.name.NameRegex;
import com.tinqinacademy.authenticationservice.api.validation.user.password.PasswordRegex;
import com.tinqinacademy.authenticationservice.api.validation.user.phone.PhoneNumberRegex;
import com.tinqinacademy.authenticationservice.api.validation.user.username.UsernameRegex;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Builder(toBuilder = true)
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RegisterInput implements OperationInput {
    @UsernameRegex
    private String username;
    @PasswordRegex
    private String password;
    @NameRegex
    private String firstName;
    @NameRegex
    private String lastName;
    @NotBlank
    @Email(message = "Invalid type of email.")
    private String email;
    @PhoneNumberRegex
    private String phoneNumber;
    @NotNull
    private LocalDate dateOfBirth;
}
