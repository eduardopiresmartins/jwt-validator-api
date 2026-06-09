package br.com.eduardo.jwtvalidator.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.eduardo.jwtvalidator.validator.ClaimsValidator;
import br.com.eduardo.jwtvalidator.validator.JwtPayloadValidator;
import br.com.eduardo.jwtvalidator.validator.JwtStructureValidator;

class JwtValidationServiceTest {

    private final JwtValidationService service = new JwtValidationService(
            new JwtStructureValidator(),
            new JwtPayloadValidator(new ObjectMapper()),
            new ClaimsValidator()
    );

    @Test
    void shouldReturnTrueWhenStructurePayloadAndClaimsAreValid() {
        boolean result = service.validate(buildToken("""
                {"Name":"Eduardo","Role":"Admin","Seed":3}
                """));

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenStructureIsValidButPayloadIsNotJson() {
        boolean result = service.validate("abc.def.ghi");

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenStructureIsInvalid() {
        boolean result = service.validate("abc.def");

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenPayloadContainsMoreThanThreeClaims() {
        boolean result = service.validate(buildToken("""
                {"Name":"Eduardo","Role":"Admin","Seed":3,"Org":"OpenAI"}
                """));

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenPayloadIsMissingRequiredClaim() {
        boolean result = service.validate(buildToken("""
                {"Name":"Eduardo","Role":"Admin"}
                """));

        assertFalse(result);
    }

    private String buildToken(String payloadJson) {
        String encodedPayload = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));

        return "header." + encodedPayload + ".signature";
    }
}
