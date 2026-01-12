package com.example.demo.modules.Race.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.modules.Race.adapter.mapper.AddressMapper;
import com.example.demo.modules.Race.application.web.dto.CoordinateResponseDTO;
import com.example.demo.modules.Race.application.web.dto.RegisterAddressDTO;
import com.example.demo.modules.Race.domain.entity.AddressEntity;
import com.example.demo.modules.Race.domain.repository.IAddressRepository;
import com.example.demo.modules.Race.infrastructure.client.NominatimClient;

import jakarta.transaction.Transactional;

@Service
public class AddressService {

    @Autowired
    private IAddressRepository addressRepository;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private NominatimClient nominatimClient;

    @Transactional
    public AddressEntity registerAddress(RegisterAddressDTO req) {

        AddressEntity entity = addressMapper.toEntity(req);

        CoordinateResponseDTO adressCoordinates = nominatimClient.fetchCoordinates(entity);

        entity.setLatitude(adressCoordinates.lat());
        entity.setLongitude(adressCoordinates.lon());

        return addressRepository.save(entity);
    }
}
