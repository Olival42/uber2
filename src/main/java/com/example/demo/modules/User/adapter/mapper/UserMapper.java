package com.example.demo.modules.User.adapter.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.example.demo.modules.User.application.web.dto.request.RegisterDriverRequestDTO;
import com.example.demo.modules.User.application.web.dto.request.RegisterPassengerRequestDTO;
import com.example.demo.modules.User.application.web.dto.response.DriverResponseDTO;
import com.example.demo.modules.User.application.web.dto.response.PassengerResponseDTO;
import com.example.demo.modules.User.application.web.dto.response.UserResponseDTO;
import com.example.demo.modules.User.domain.entity.DriverEntity;
import com.example.demo.modules.User.domain.entity.PassengerEntity;
import com.example.demo.modules.User.domain.entity.UserEntity;

@Mapper(componentModel = "spring",  unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pixKey", ignore = true)
    @Mapping(target = "pixKeyType", ignore = true)
    @Mapping(target = "role", constant = "DRIVER")
    @Mapping(target = "active", constant = "true")
    DriverEntity toDriverEntity(RegisterDriverRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", constant = "PASSENGER")
    @Mapping(target = "active", constant = "true")
    PassengerEntity toPassengerEntity(RegisterPassengerRequestDTO dto);

    UserResponseDTO toUserResponseDto(UserEntity entity);

    DriverResponseDTO toDriverResponseDto(DriverEntity entity);

    PassengerResponseDTO toPassengerResponseDto(PassengerEntity entity);
}
