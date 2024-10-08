package com.tinqinacademy.authenticationservice.api.validation.user.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PasswordRegexValidation implements ConstraintValidator<PasswordRegex,String> {
    @Override
    public void initialize(PasswordRegex constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        //* At least one digit (0-9).
        //* At least one lowercase letter (a-z).
        //* At least one uppercase letter (A-Z).
        //* At least one special character from the provided set.
        //* No whitespace characters.
        //* The overall length of the password must be between 8 and 30 characters.
        //! The password should contain only ASCII printable characters
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_*~!)(./:;?{}|`',-])[0-9a-zA-Z@#$%^&+=_*~!)(./:;?{}|`',-]{8,30}$";

        Pattern p = Pattern.compile(regex);
        if(password == null) {return false;}
        else if(StringUtils.isAsciiPrintable(password))
        {
            Matcher m = p.matcher(password);
            return m.matches();
        }
        else {return false;}
    }
}
