package br.com.eduardo.jwtvalidator.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(JwtValidationService.class);

    private final JwtStructureValidator jwtStructureValidator;
    private final JwtPayloadValidator jwtPayloadValidator;
    private final ClaimsValidator claimsValidator;
    private final NameClaimValidator nameClaimValidator;
    private final RoleClaimValidator roleClaimValidator;
    private final SeedClaimValidator seedClaimValidator;

    public boolean validate(String token) {
        String executionId = UUID.randomUUID().toString().substring(0, 8);
        log.info("[{}] Iniciando validacao de JWT", executionId);

        if (!jwtStructureValidator.isValid(token)) {
            log.warn("[{}] Falha na validacao do JWT: estrutura do token invalida", executionId);
            return false;
        }

        JsonNode payload = jwtPayloadValidator.extractPayload(token);
        if (payload == null) {
            log.warn("[{}] Falha na validacao do JWT: payload invalido", executionId);
            return false;
        }

        if (!claimsValidator.isValid(payload)) {
            log.warn("[{}] Falha na validacao do JWT: claims obrigatorias invalidas", executionId);
            return false;
        }

        if (!nameClaimValidator.isValid(payload)) {
            log.warn("[{}] Falha na validacao do JWT: claim Name invalida", executionId);
            return false;
        }

        if (!roleClaimValidator.isValid(payload)) {
            log.warn("[{}] Falha na validacao do JWT: claim Role invalida", executionId);
            return false;
        }

        if (!seedClaimValidator.isValid(payload)) {
            log.warn("[{}] Falha na validacao do JWT: claim Seed invalida", executionId);
            return false;
        }

        log.info("[{}] Validacao de JWT concluida com sucesso", executionId);
        return true;
    }
}
