package br.com.eduardo.jwtvalidator.service;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

import br.com.eduardo.jwtvalidator.validator.ClaimsValidator;
import br.com.eduardo.jwtvalidator.validator.JwtPayloadValidator;
import br.com.eduardo.jwtvalidator.validator.JwtStructureValidator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtValidationService {

    private final JwtStructureValidator jwtStructureValidator;
    private final JwtPayloadValidator jwtPayloadValidator;
    private final ClaimsValidator claimsValidator;

    public boolean validate(String token) {
        if (!jwtStructureValidator.isValid(token)) {
            return false;
        }

        JsonNode payload = jwtPayloadValidator.extractPayload(token);
        if (payload == null) {
            return false;
        }

        return claimsValidator.isValid(payload);
    }
}
