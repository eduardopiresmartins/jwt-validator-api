package br.com.eduardo.jwtvalidator.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.eduardo.jwtvalidator.dto.JwtValidationRequest;
import br.com.eduardo.jwtvalidator.dto.JwtValidationResponse;
import br.com.eduardo.jwtvalidator.service.JwtValidationService;
import br.com.eduardo.jwtvalidator.validator.ClaimsValidator;
import br.com.eduardo.jwtvalidator.validator.JwtPayloadValidator;
import br.com.eduardo.jwtvalidator.validator.JwtStructureValidator;
import br.com.eduardo.jwtvalidator.validator.NameClaimValidator;

class JwtValidationControllerTest {

    private final JwtValidationController controller = new JwtValidationController(
            new JwtValidationService(
                    new JwtStructureValidator(),
                    new JwtPayloadValidator(new ObjectMapper()),
                    new ClaimsValidator(),
                    new NameClaimValidator()
            )
    );

    @Test
    void shouldReturnTrueWhenRequestContainsTokenWithValidRequiredClaims() {
        ResponseEntity<JwtValidationResponse> response = controller.validate(
                new JwtValidationRequest(buildToken("""
                        {"Name":"Eduardo","Role":"Admin","Seed":3}
                        """))
        );

        assertTrue(response.getBody().valid());
    }

    @Test
    void shouldReturnFalseWhenRequestContainsTokenWithLessThanThreeParts() {
        ResponseEntity<JwtValidationResponse> response = controller.validate(new JwtValidationRequest("abc.def"));

        assertFalse(response.getBody().valid());
    }

    @Test
    void shouldReturnFalseWhenRequestContainsTokenWithInvalidJsonPayload() {
        ResponseEntity<JwtValidationResponse> response = controller.validate(new JwtValidationRequest("abc.def.ghi"));

        assertFalse(response.getBody().valid());
    }

    @Test
    void shouldReturnFalseWhenRequestContainsTokenWithExtraClaim() {
        ResponseEntity<JwtValidationResponse> response = controller.validate(
                new JwtValidationRequest(buildToken("""
                        {"Name":"Eduardo","Role":"Admin","Seed":3,"Org":"OpenAI"}
                        """))
        );

        assertFalse(response.getBody().valid());
    }

    private String buildToken(String payloadJson) {
        String encodedPayload = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));

        return "header." + encodedPayload + ".signature";
    }
}
