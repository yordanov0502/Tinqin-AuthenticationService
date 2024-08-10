package com.tinqinacademy.authenticationservice.core.utils;

import com.tinqinacademy.authenticationservice.persistence.repository.AccountCodeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CodeGenerator {

    private final AccountCodeRepository accountCodeRepository;

    public String generateRandomCode(){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int length = 12;
        String randomCode;
        do {
            randomCode = RandomStringUtils.random(length, characters);
        }
        while(accountCodeRepository.existsByCode(randomCode));
        return randomCode;
    }
}
