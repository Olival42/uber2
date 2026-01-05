package com.example.demo.utils;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieManager {

    public ResponseCookie createRefreshCookie(String refreshToken, long expiresAt) {
                return ResponseCookie.from("refreshToken", refreshToken)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(expiresAt)
                                .sameSite("Strict")
                                .build();
    }

    public ResponseCookie deleteRefreshCookie() {
                return ResponseCookie.from("refreshToken", "")
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(0)
                                .sameSite("Strict")
                                .build();
    }
}
