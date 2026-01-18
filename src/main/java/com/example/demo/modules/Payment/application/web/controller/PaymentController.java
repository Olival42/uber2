package com.example.demo.modules.Payment.application.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.modules.Payment.domain.service.PixService;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PixService pixService;

    @GetMapping("/pix/qr-code")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<byte[]> generatePixQrCode() {

        byte[] qrCodeImage = pixService.generatePixQrCodeForLoggedDriver();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE,MediaType.IMAGE_PNG_VALUE)
                .body(qrCodeImage);
    }
}
