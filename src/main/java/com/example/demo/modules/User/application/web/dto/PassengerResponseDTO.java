package com.example.demo.modules.User.application.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class PassengerResponseDTO extends UserResponseDTO {

    private String cpf;
    private String phone;
}
