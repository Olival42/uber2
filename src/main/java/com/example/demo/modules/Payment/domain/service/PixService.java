package com.example.demo.modules.Payment.domain.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.infrastructure.exception.BusinessRuleException;
import com.example.demo.modules.Race.domain.entity.RideEntity;
import com.example.demo.modules.Race.domain.repository.IRideRepository;
import com.example.demo.modules.User.domain.entity.DriverEntity;
import com.example.demo.modules.User.domain.repository.IDriverRepository;
import com.example.demo.shared.pix.PixPayloadGenerator;
import com.example.demo.shared.security.SecurityUtils;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PixService {

    private static final String MERCHANT_CITY = "ONLINE";

    @Autowired
    private PixQrCodeService pixQrCodeService;

    @Autowired
    private IDriverRepository driverRepository;

    @Autowired
    private IRideRepository rideRepository;

    public byte[] generatePixQrCodeForLoggedDriver() {

        String email = SecurityUtils.getEmail();

        DriverEntity driver = driverRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found"));

        if (driver.getPixKey() == null || driver.getPixKey().isBlank()) {
            throw new BusinessRuleException("Driver does not have a registered PIX key");
        }

        RideEntity ride = rideRepository
                .findFirstByDriverIdOrderByFinishedAtDesc(driver.getId())
                .orElseThrow(() -> new BusinessRuleException("Driver has no completed rides"));

        String pixKey = driver.getPixKey();
        String merchantName = driver.getName();
        String txid = "TXID-" + ride.getId();
        BigDecimal amount = new BigDecimal(ride.getPrice());

        String payload = PixPayloadGenerator.generate(pixKey, merchantName, MERCHANT_CITY, txid, amount);

        return pixQrCodeService.generateQrCode(payload);
    }
}
