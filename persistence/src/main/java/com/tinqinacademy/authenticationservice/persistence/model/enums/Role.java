package com.tinqinacademy.authenticationservice.persistence.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;

public enum Role {
    ADMIN("admin"),
    USER("user"),
    UNKNOWN("");

    private final String code;

    Role(String code){
        this.code=code;
    }

    @JsonCreator
    public static Role getByCode(String code){

        return Arrays.stream(Role.values())
                .filter(role -> role.code.equals(code))
                .findFirst()
                .orElse(UNKNOWN);
    }


    @JsonValue
    public String toString(){
        return code;
    }

}
