package com.tinqinacademy.authenticationservice.api.validation.user.name;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class NameRegexValidation implements ConstraintValidator<NameRegex,String> {
    @Override
    public void initialize(NameRegex constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        //* Name should start with capital letter[А-ЯA-Z] and contain only lowercase latin or cyrillic letters[а-яa-z].
        String regex = "^[А-ЯA-Z][а-яa-z]{2,19}$";

        Pattern p = Pattern.compile(regex);
        if(name == null) {return false;}
        else
        {
            Matcher m = p.matcher(name);
            return m.matches();
        }
    }
}