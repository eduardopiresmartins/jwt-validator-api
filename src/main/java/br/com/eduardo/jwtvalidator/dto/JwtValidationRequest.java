package br.com.eduardo.jwtvalidator.dto;

import jakarta.validation.constraints.NotBlank;

public record JwtValidationRequest(
        @NotBlank(message = "Token is required")
        String token
) {
}