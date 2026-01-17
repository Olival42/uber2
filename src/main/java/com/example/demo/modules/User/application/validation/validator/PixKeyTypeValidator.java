package com.example.demo.modules.User.application.validation.validator;

import org.springframework.stereotype.Component;

import com.example.demo.infrastructure.exception.BusinessRuleException;
import com.example.demo.modules.User.domain.enums.PixKeyType;
import com.example.demo.shared.validation.validator.CNPJValidator;
import com.example.demo.shared.validation.validator.CPFValidator;
import com.example.demo.shared.validation.validator.EmailValidator;
import com.example.demo.shared.validation.validator.PhoneValidator;

@Component
public class PixKeyTypeValidator {

    public void validate(String pixKey, PixKeyType pixKeyType) {

        switch (pixKeyType) {

            case CPF -> {
                if (!CPFValidator.isValid(pixKey))
                    throw new BusinessRuleException("Invalid CPF pix key");
            }
            case CNPJ -> {
                if (!CNPJValidator.isValid(pixKey))
                    throw new BusinessRuleException("Invalid CNPJ pix key");
            }
            case EMAIL -> {
                if (!EmailValidator.isValid(pixKey))
                    throw new BusinessRuleException("Invalid email pix key");
            }
            case PHONE -> {
                if (!PhoneValidator.isValid(pixKey))
                    throw new BusinessRuleException("Invalid phone pix key");
            }
        }
    }
}
