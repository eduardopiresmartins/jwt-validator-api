package br.com.eduardo.jwtvalidator.validator;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

@Component
public class RoleClaimValidator {

    private static final Set<String> ALLOWED_ROLES = Set.of("Admin", "Member", "External");

    public boolean isValid(JsonNode payload) {
        if (payload == null || !payload.isObject()) {
            return false;
        }

        JsonNode roleNode = payload.get("Role");
        if (roleNode == null || roleNode.isNull() || !roleNode.isTextual()) {
            return false;
        }

        String role = roleNode.asText();
        return !role.isBlank() && ALLOWED_ROLES.contains(role);
    }
}
