package com.tinqinacademy.authenticationservice.rest.security;

import com.tinqinacademy.authenticationservice.persistence.model.entity.User;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Getter
@Component
@RequestScope
public class UserContext {
    private User currAuthorizedUser;
    private String jwt;

    public void setContext(User user, String jwt){
        this.currAuthorizedUser = user;
        this.jwt = jwt;
    }
}