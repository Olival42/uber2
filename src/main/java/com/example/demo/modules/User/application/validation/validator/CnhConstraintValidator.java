package com.example.demo.modules.User.application.validation.validator;

import com.example.demo.modules.User.application.validation.anotation.ValidCnh;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CnhConstraintValidator
        implements ConstraintValidator<ValidCnh, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Remove espaços
        String cnh = value.trim();

        // Deve ter 11 dígitos numéricos
        if (!cnh.matches("\\d{11}")) {
            return false;
        }

        // CNHs inválidas conhecidas (todos dígitos iguais)
        if (cnh.matches("(\\d)\\1{10}")) {
            return false;
        }

        int soma = 0;
        int peso = 9;

        // Primeiro dígito verificador
        for (int i = 0; i < 9; i++) {
            soma += (cnh.charAt(i) - '0') * peso--;
        }

        int digito1 = soma % 11;
        digito1 = (digito1 >= 10) ? 0 : digito1;

        // Segundo dígito verificador
        soma = 0;
        peso = 1;

        for (int i = 0; i < 9; i++) {
            soma += (cnh.charAt(i) - '0') * peso++;
        }

        int digito2 = soma % 11;
        digito2 = (digito2 >= 10) ? 0 : digito2;

        // Verificação final
        return digito1 == (cnh.charAt(9) - '0')
                && digito2 == (cnh.charAt(10) - '0');
    }
}
