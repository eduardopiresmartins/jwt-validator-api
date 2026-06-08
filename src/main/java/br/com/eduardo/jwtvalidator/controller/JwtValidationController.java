package br.com.eduardo.jwtvalidator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.eduardo.jwtvalidator.dto.JwtValidationRequest;
import br.com.eduardo.jwtvalidator.dto.JwtValidationResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/jwt")
public class JwtValidationController {

    @PostMapping("/validate")
    public ResponseEntity<JwtValidationResponse> validate(
            @Valid @RequestBody JwtValidationRequest request
    ) {
        return ResponseEntity.ok(new JwtValidationResponse(false));
    }
}