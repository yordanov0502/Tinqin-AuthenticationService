package com.tinqinacademy.authenticationservice.persistence.model.context;

import com.tinqinacademy.authenticationservice.persistence.model.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Getter
@Setter
@Component
@RequestScope
public class UserContext {
    private User currAuthorizedUser;
    private String jwt;
}