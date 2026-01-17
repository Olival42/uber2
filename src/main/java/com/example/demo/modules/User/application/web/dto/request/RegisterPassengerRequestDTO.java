package com.example.demo.modules.User.application.web.dto.request;

import org.hibernate.validator.constraints.Length;

import com.example.demo.modules.User.application.validation.anotation.ValidCpf;
import com.example.demo.modules.User.application.validation.anotation.ValidPassword;
import com.example.demo.modules.User.application.validation.anotation.ValidPhone;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RegisterPassengerRequestDTO {
@NotBlank(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    private String email;

    @Length(min = 8, message = "Password must be at least 8 characters long")
    @NotBlank(message = "Password cannot be null")
    @ValidPassword(message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
    private String password;

    @NotBlank(message = "Name cannot be null")
    private String name;

    @NotBlank(message = "CPF cannot be null")
    @ValidCpf
    private String cpf;

    @NotBlank(message = "Phone cannot be null")
    @ValidPhone(message = "Phone invalid. Use XXXXXXXXXX or XX9XXXXXXXX")
    private String phone;
}
