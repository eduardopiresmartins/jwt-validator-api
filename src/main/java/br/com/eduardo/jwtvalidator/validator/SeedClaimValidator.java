package br.com.eduardo.jwtvalidator.validator;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

@Component
public class SeedClaimValidator {

    public boolean isValid(JsonNode payload) {
        if (payload == null || !payload.isObject()) {
            return false;
        }

        JsonNode seedNode = payload.get("Seed");
        if (seedNode == null || seedNode.isNull()) {
            return false;
        }

        String seedValue = extractSeedValue(seedNode);
        if (seedValue == null || seedValue.isBlank() || !seedValue.chars().allMatch(Character::isDigit)) {
            return false;
        }

        try {
            long seedNumber = Long.parseLong(seedValue);
            return isPrime(seedNumber);
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    private String extractSeedValue(JsonNode seedNode) {
        if (seedNode.isTextual()) {
            return seedNode.asText();
        }

        if (seedNode.isIntegralNumber()) {
            return seedNode.asText();
        }

        return null;
    }

    private boolean isPrime(long number) {
        if (number <= 1) {
            return false;
        }

        if (number == 2) {
            return true;
        }

        if (number % 2 == 0) {
            return false;
        }

        for (long divisor = 3; divisor * divisor <= number; divisor += 2) {
            if (number % divisor == 0) {
                return false;
            }
        }

        return true;
    }
}
