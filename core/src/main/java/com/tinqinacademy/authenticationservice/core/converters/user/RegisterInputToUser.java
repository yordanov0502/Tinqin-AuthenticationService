package com.tinqinacademy.authenticationservice.core.converters.user;

import com.tinqinacademy.authenticationservice.api.operations.register.RegisterInput;
import com.tinqinacademy.authenticationservice.core.converters.BaseConverter;
import com.tinqinacademy.authenticationservice.persistence.model.entity.User;
import com.tinqinacademy.authenticationservice.persistence.model.enums.Role;
import org.springframework.stereotype.Component;

@Component
public class RegisterInputToUser extends BaseConverter<RegisterInput, User.UserBuilder, RegisterInputToUser> {
    public RegisterInputToUser() {
        super(RegisterInputToUser.class);
    }

    @Override
    protected User.UserBuilder convertObj(RegisterInput input) {

        User.UserBuilder newUser = User.builder()
                .role(Role.USER)
                .username(input.getUsername())
                .email(input.getEmail())
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .phoneNumber(input.getPhoneNumber())
                .dateOfBirth(input.getDateOfBirth())
                .isConfirmed(false);

        return newUser;
    }
}
