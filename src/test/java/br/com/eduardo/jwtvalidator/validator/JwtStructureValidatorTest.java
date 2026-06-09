package br.com.eduardo.jwtvalidator.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class JwtStructureValidatorTest {

    private final JwtStructureValidator validator = new JwtStructureValidator();

    @Test
    void shouldReturnTrueWhenTokenHasThreeNonEmptyParts() {
        boolean result = validator.isValid("abc.def.ghi");

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenTokenIsNull() {
        boolean result = validator.isValid(null);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenTokenIsBlank() {
        boolean result = validator.isValid("   ");

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenTokenHasLessThanThreeParts() {
        boolean result = validator.isValid("abc.def");

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenTokenHasMoreThanThreeParts() {
        boolean result = validator.isValid("abc.def.ghi.jkl");

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenAnyTokenPartIsEmpty() {
        assertFalse(validator.isValid(".def.ghi"));
        assertFalse(validator.isValid("abc..ghi"));
        assertFalse(validator.isValid("abc.def."));
    }
}