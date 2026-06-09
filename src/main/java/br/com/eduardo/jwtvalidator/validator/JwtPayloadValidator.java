package br.com.eduardo.jwtvalidator.validator;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
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
        return extractPayload(token) != null;
    }

    public JsonNode extractPayload(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }

        String[] parts = token.split(JWT_SEPARATOR, -1);
        if (parts.length != JWT_PARTS_COUNT) {
            return null;
        }

        try {
            byte[] decodedPayload = Base64.getUrlDecoder().decode(parts[PAYLOAD_INDEX]);
            return objectMapper.readTree(new String(decodedPayload, StandardCharsets.UTF_8));
        } catch (Exception exception) {
            return null;
        }
    }
}
