package br.com.eduardo.jwtvalidator.validator;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtPayloadValidator {

    private static final int PAYLOAD_INDEX = 1;
    private static final int JWT_PARTS_COUNT = 3;
    private static final String JWT_SEPARATOR = "\\.";

    private final ObjectMapper objectMapper;

    public boolean isValid(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }

        String[] parts = token.split(JWT_SEPARATOR, -1);
        if (parts.length != JWT_PARTS_COUNT) {
            return false;
        }

        try {
            byte[] decodedPayload = Base64.getUrlDecoder().decode(parts[PAYLOAD_INDEX]);
            objectMapper.readTree(new String(decodedPayload, StandardCharsets.UTF_8));
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}
