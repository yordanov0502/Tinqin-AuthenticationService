package com.tinqinacademy.authenticationservice.core.utils;

import com.tinqinacademy.authenticationservice.api.exceptions.custom.PasswordException;
import com.tinqinacademy.authenticationservice.persistence.repository.AccountCodeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class ContentGenerator {

    private final AccountCodeRepository accountCodeRepository;
    private final PasswordRecoveryCodesCacheSerivce passwordRecoveryCodesCacheSerivce;

    public String generateRandomCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int length = 12;
        String randomCode;
        do {
            randomCode = RandomStringUtils.random(length, characters);
        }
        while (accountCodeRepository.existsByCode(randomCode));
        return randomCode;
    }

    public String generatePasswordRecoveryCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXY0123456789";
        int length = 12;
        String passwordRecoveryCode;
        do{
            passwordRecoveryCode = RandomStringUtils.random(length,characters);
        }
        while (passwordRecoveryCodesCacheSerivce.existsInCache(passwordRecoveryCode));
        return passwordRecoveryCode;
    }

}