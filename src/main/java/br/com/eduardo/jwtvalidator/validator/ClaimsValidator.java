package br.com.eduardo.jwtvalidator.validator;

import java.util.Iterator;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

@Component
public class ClaimsValidator {

    private static final Set<String> REQUIRED_CLAIMS = Set.of("Name", "Role", "Seed");

    public boolean isValid(JsonNode payload) {
        if (payload == null || !payload.isObject() || payload.isEmpty() || payload.size() != REQUIRED_CLAIMS.size()) {
            return false;
        }

        for (String requiredClaim : REQUIRED_CLAIMS) {
            if (!payload.has(requiredClaim)) {
                return false;
            }
        }

        Iterator<String> fieldNames = payload.fieldNames();
        while (fieldNames.hasNext()) {
            if (!REQUIRED_CLAIMS.contains(fieldNames.next())) {
                return false;
            }
        }

        return true;
    }
}
