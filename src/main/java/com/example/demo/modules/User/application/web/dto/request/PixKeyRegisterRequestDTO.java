package com.example.demo.modules.User.application.web.dto.request;

import com.example.demo.modules.User.domain.enums.PixKeyType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PixKeyRegisterRequestDTO {

    @NotBlank(message = "Pix key cannot be null")
    private String key;

    @NotNull(message = "Pix key type cannot be null")
    private PixKeyType type;
}
