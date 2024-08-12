package com.tinqinacademy.authenticationservice.api.validation.user.dateofbirth;


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
@Constraint(validatedBy = DateOfBirthValidation.class)
public @interface DateOfBirth {
    String message() default "Only individuals aged 18 and above are eligible to register.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
