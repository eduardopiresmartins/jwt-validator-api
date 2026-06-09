package br.com.eduardo.jwtvalidator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.eduardo.jwtvalidator.dto.JwtValidationRequest;
import br.com.eduardo.jwtvalidator.dto.JwtValidationResponse;
import br.com.eduardo.jwtvalidator.service.JwtValidationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/jwt")
@RequiredArgsConstructor
public class JwtValidationController {

    private final JwtValidationService jwtValidationService;

    @PostMapping("/validate")
    public ResponseEntity<JwtValidationResponse> validate(
            @Valid @RequestBody JwtValidationRequest request
    ) {
        return ResponseEntity.ok(new JwtValidationResponse(jwtValidationService.validate(request.token())));
    }
}
