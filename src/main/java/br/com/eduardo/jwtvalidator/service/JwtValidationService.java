package br.com.eduardo.jwtvalidator.service;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import br.com.eduardo.jwtvalidator.validator.ClaimsValidator;
import br.com.eduardo.jwtvalidator.validator.JwtPayloadValidator;
import br.com.eduardo.jwtvalidator.validator.JwtStructureValidator;
import br.com.eduardo.jwtvalidator.validator.NameClaimValidator;
import br.com.eduardo.jwtvalidator.validator.RoleClaimValidator;
import br.com.eduardo.jwtvalidator.validator.SeedClaimValidator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtValidationService {

    private final JwtStructureValidator jwtStructureValidator;
    private final JwtPayloadValidator jwtPayloadValidator;
    private final ClaimsValidator claimsValidator;
    private final NameClaimValidator nameClaimValidator;
    private final RoleClaimValidator roleClaimValidator;
    private final SeedClaimValidator seedClaimValidator;

    public boolean validate(String token) {
        if (!jwtStructureValidator.isValid(token)) {
            return false;
        }

        JsonNode payload = jwtPayloadValidator.extractPayload(token);
        if (payload == null) {
            return false;
        }

        return claimsValidator.isValid(payload)
                && nameClaimValidator.isValid(payload)
                && roleClaimValidator.isValid(payload)
                && seedClaimValidator.isValid(payload);
    }
}
