package com.example.demo.modules.User.application.web.dto;

import org.hibernate.validator.constraints.Length;

import com.example.demo.modules.User.application.validation.anotation.ValidCpf;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RegisterPassengerDTO {
@NotBlank(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    private String email;

    @Length(min = 8, message = "Password must be at least 8 characters long")
    @NotBlank(message = "Password cannot be null")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
    private String password;

    @NotBlank(message = "Name cannot be null")
    private String name;

    @NotBlank(message = "CPF cannot be null")
    @ValidCpf
    private String cpf;

    @NotBlank(message = "Phone cannot be null")
    @Pattern(regexp = "\\d{2}\\d{4,5}\\d{4}", message = "phone invalide. Use XXXXXXXXXX or XX9XXXXXXXX")
    private String phone;
}
