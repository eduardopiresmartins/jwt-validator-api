package br.com.eduardo.jwtvalidator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.eduardo.jwtvalidator.dto.ErrorResponse;
import br.com.eduardo.jwtvalidator.dto.JwtValidationRequest;
import br.com.eduardo.jwtvalidator.dto.JwtValidationResponse;
import br.com.eduardo.jwtvalidator.service.JwtValidationService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/jwt")
@RequiredArgsConstructor
public class JwtValidationController {

    private final JwtValidationService jwtValidationService;

    @PostMapping("/validate")
    @Operation(
            summary = "Valida um JWT",
            description = "Recebe um token JWT e retorna se ele atende ou nao as regras de validacao definidas pela aplicacao."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Resultado da validacao do JWT retornando valid como true ou false.",
                    content = @Content(schema = @Schema(implementation = JwtValidationResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request body ausente, malformado ou com token vazio.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<JwtValidationResponse> validate(
            @Valid @RequestBody JwtValidationRequest request
    ) {
        return ResponseEntity.ok(new JwtValidationResponse(jwtValidationService.validate(request.token())));
    }
}
