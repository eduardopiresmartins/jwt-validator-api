package br.com.eduardo.jwtvalidator.service;

import org.springframework.stereotype.Service;

import br.com.eduardo.jwtvalidator.validator.JwtPayloadValidator;
import br.com.eduardo.jwtvalidator.validator.JwtStructureValidator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtValidationService {

    private final JwtStructureValidator jwtStructureValidator;
    private final JwtPayloadValidator jwtPayloadValidator;

    public boolean validate(String token) {
        return jwtStructureValidator.isValid(token) && jwtPayloadValidator.isValid(token);
    }
}
