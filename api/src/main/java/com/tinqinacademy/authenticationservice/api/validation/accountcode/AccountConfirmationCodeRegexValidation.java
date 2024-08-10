package com.tinqinacademy.authenticationservice.api.validation.accountcode;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AccountConfirmationCodeRegexValidation implements ConstraintValidator<AccountConfirmationCodeRegex, String> {
    @Override
    public void initialize(AccountConfirmationCodeRegex constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String confirmationCode, ConstraintValidatorContext context) {
        String regex = "^[A-Za-z0-9]{12}$";
        Pattern p = Pattern.compile(regex);

        if (confirmationCode == null) {
            return false;
        } else {
            Matcher m = p.matcher(confirmationCode);
            return m.matches();
        }
    }
}