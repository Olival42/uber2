package com.example.demo.modules.User.adapter.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.example.demo.modules.User.application.web.dto.DriverResponseDTO;
import com.example.demo.modules.User.application.web.dto.PassengerResponseDTO;
import com.example.demo.modules.User.application.web.dto.RegisterDriverDTO;
import com.example.demo.modules.User.application.web.dto.RegisterPassengerDTO;
import com.example.demo.modules.User.application.web.dto.UserResponseDTO;
import com.example.demo.modules.User.domain.entity.DriverEntity;
import com.example.demo.modules.User.domain.entity.PassengerEntity;
import com.example.demo.modules.User.domain.entity.UserEntity;

@Mapper(componentModel = "spring",  unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", constant = "DRIVER")
    @Mapping(target = "active", constant = "true")
    DriverEntity toDriverEntity(RegisterDriverDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", constant = "PASSENGER")
    @Mapping(target = "active", constant = "true")
    PassengerEntity toPassengerEntity(RegisterPassengerDTO dto);

    UserResponseDTO toUserResponseDto(UserEntity entity);

    DriverResponseDTO toDriverResponseDto(DriverEntity entity);

    PassengerResponseDTO toPassengerResponseDto(PassengerEntity entity);
}
