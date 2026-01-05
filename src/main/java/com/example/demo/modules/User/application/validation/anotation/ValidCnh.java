package com.example.demo.modules.User.application.validation.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.demo.modules.User.application.validation.validator.CnhConstraintValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = CnhConstraintValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCnh {
    String message() default "CNH invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
