package com.tinqinacademy.authenticationservice.api.validation.user;

import com.tinqinacademy.authenticationservice.api.validation.user.annotation.PhoneNumberRegex;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PhoneNumberValidation implements ConstraintValidator<PhoneNumberRegex,String> {

    @Override
    public void initialize(PhoneNumberRegex constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        //* Phone number should only contain digits [0-9].
        //* Length should be between 10 and 16 characters.
        String regex = "^[0-9]{10,16}$";

        Pattern pattern = Pattern.compile(regex);
        if (phoneNumber == null) {
            return false;
        } else {
            Matcher matcher = pattern.matcher(phoneNumber);
            return matcher.matches();
        }
    }
}
