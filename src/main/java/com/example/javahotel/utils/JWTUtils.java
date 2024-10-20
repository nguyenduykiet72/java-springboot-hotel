package com.example.javahotel.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class JWTUtils {
    private static final long EXPIRATION_TIME = 1000 * 60 * 24 * 7; // 7 days
    private final SecretKey Key;

    public JWTUtils(@Value("${jwt.secret}") String secreteString) {
        byte[] keyBytes = Base64.getDecoder().decode(secreteString.getBytes(StandardCharsets.UTF_8));
        this.Key = new SecretKeySpec(keyBytes, "HmacSHA256");
    }
}
