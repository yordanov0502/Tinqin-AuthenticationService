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

    public String generateRandomPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%^&+=_*~!)(./:;?{}|`',-";
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_*~!)(./:;?{}|`',-])[0-9a-zA-Z@#$%^&+=_*~!)(./:;?{}|`',-]{8,30}$";
        Pattern pattern = Pattern.compile(regex);
        int counter = 0;

        while (true)
        {
            if(counter>=300) {throw new PasswordException("Unexpected error occurred. Please try again.");}
            else
            {
                counter++;
                String generatedPassword = RandomStringUtils.random(12, characters);
                Matcher matcher = pattern.matcher(generatedPassword);
                if(matcher.matches()) {
                    return generatedPassword;}
            }
        }
    }

}