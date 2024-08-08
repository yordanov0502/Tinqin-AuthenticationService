package com.tinqinacademy.authenticationservice.api.validation.user.annotation;


import com.tinqinacademy.authenticationservice.api.validation.user.PhoneNumberValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target({FIELD,TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PhoneNumberValidation.class)
public @interface PhoneNumberRegex {
    String message() default "Invalid type of email.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
