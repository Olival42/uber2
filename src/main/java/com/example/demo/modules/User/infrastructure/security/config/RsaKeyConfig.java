package com.example.demo.modules.User.infrastructure.security.config;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.security.KeyFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class RsaKeyConfig {

    private byte[] readPemFromClassPath(String path) throws Exception {
        ClassPathResource pathResource = new ClassPathResource(path);
        try (InputStream is = pathResource.getInputStream()) {
            String pem = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            pem = pem.replaceAll("-----BEGIN [A-Z ]+-----", "")
                    .replaceAll("-----END [A-Z ]+-----", "")
                    .replaceAll("\\s", "");

            return Base64.getDecoder().decode(pem);
        }
    }

    @Bean
    public PrivateKey privateKey() throws Exception {
        byte[] keyBytes = readPemFromClassPath("keys/private_key.pem");
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    @Bean
    public PublicKey publicKey() throws Exception {
        byte[] keyBytes = readPemFromClassPath("keys/public_key.pem");
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }
}