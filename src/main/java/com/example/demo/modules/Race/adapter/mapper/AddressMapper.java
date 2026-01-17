package com.example.demo.modules.Race.adapter.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.example.demo.modules.Race.application.web.dto.request.RegisterAddressRequestDTO;
import com.example.demo.modules.Race.domain.entity.AddressEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "latitude", ignore = true)
    @Mapping(target = "longitude", ignore = true)
    AddressEntity toEntity(RegisterAddressRequestDTO dto);

}
