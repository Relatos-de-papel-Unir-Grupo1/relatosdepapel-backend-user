package com.Unir.RelatosdePapel.User.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Unir.RelatosdePapel.User.controller.dto.LoginRequest;
import com.Unir.RelatosdePapel.User.controller.dto.TokenResponse;
import com.Unir.RelatosdePapel.User.service.AuthService;
import com.Unir.RelatosdePapel.User.service.model.SessionValidationResponse;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("api/v1/tokens")
@RequiredArgsConstructor
public class TokenController {
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<?> createToken(@RequestBody LoginRequest loginRequest) {
        
        Optional<String> tokenIdOptional = authService.createPhanthomToken(loginRequest.getUsername(), loginRequest.getPassword());

        if (tokenIdOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Datos de autenticación incorrectos");
        }

        return ResponseEntity.ok().body(tokenIdOptional.get());
    }

    @PostMapping("/{tokenId}/renewals")
    public ResponseEntity<?> refreshToken(@PathVariable String tokenId) {
        Optional<String> tokenIdOptional = authService.refreshPhantomToken(tokenId);
        
        if (tokenIdOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o expirado");
        }

        return ResponseEntity.ok(new TokenResponse(tokenIdOptional.get()));
    }

    @GetMapping("/{tokenId}")
    public ResponseEntity<?> ValidateToken(@PathVariable String tokenId) {
        SessionValidationResponse result = authService.validatePhantomToken(tokenId);

       return switch (result.status()) {
            case VALID -> ResponseEntity.ok(new TokenResponse(result.redisSessionData().getAccessToken()));
            case EXPIRED, NOT_FOUND -> ResponseEntity.status(HttpStatus.GONE).build();
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        };
    }
    
    
    
}
