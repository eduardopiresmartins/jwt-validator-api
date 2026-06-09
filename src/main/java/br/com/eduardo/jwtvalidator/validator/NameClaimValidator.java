package br.com.eduardo.jwtvalidator.validator;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

@Component
public class NameClaimValidator {

    private static final int MAX_NAME_LENGTH = 256;

    public boolean isValid(JsonNode payload) {
        if (payload == null || !payload.isObject()) {
            return false;
        }

        JsonNode nameNode = payload.get("Name");
        if (nameNode == null || nameNode.isNull() || !nameNode.isTextual()) {
            return false;
        }

        String name = nameNode.asText();
        if (name.isBlank() || name.length() > MAX_NAME_LENGTH) {
            return false;
        }

        return name.chars().noneMatch(Character::isDigit);
    }
}
