package com.example.demo.modules.User.application.validation.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.demo.modules.User.application.validation.validator.CpfConstraintValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = CpfConstraintValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCpf {

    String message() default "Cpf inválido";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
