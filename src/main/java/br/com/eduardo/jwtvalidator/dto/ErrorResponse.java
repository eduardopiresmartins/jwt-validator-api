package br.com.eduardo.jwtvalidator.dto;

import java.util.List;

public record ErrorResponse(
        String message,
        List<String> details
) {
}
