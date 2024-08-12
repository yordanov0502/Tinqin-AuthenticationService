package com.tinqinacademy.authenticationservice.api.validation.user.dateofbirth;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

@Component
public class DateOfBirthValidation implements ConstraintValidator<DateOfBirth, LocalDate> {
    @Override
    public void initialize(DateOfBirth constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDate dateOfBirth, ConstraintValidatorContext constraintValidatorContext) {
        if (dateOfBirth == null) {
            return false;
        }

        LocalDate today = LocalDate.now();
        Period age = Period.between(dateOfBirth, today);

        return age.getYears() >= 18;
    }

}