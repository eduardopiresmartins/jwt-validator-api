package br.com.eduardo.jwtvalidator.validator;

import org.springframework.stereotype.Component;

@Component
public class JwtStructureValidator {

    private static final int JWT_PARTS_COUNT = 3;
    private static final String JWT_SEPARATOR = "\\.";

    public boolean isValid(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }

        String[] parts = token.split(JWT_SEPARATOR, -1);

        if (parts.length != JWT_PARTS_COUNT) {
            return false;
        }

        for (String part : parts) {
            if (part == null || part.isBlank()) {
                return false;
            }
        }

        return true;
    }
}