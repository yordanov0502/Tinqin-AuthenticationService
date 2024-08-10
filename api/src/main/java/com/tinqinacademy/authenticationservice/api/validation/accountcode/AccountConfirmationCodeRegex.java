package com.tinqinacademy.authenticationservice.api.validation.accountcode;


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
@Constraint(validatedBy = AccountConfirmationCodeRegexValidation.class)
public @interface AccountConfirmationCodeRegex {
    String message() default "Invalid confirmation code.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
