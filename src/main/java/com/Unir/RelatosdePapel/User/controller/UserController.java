package com.Unir.RelatosdePapel.User.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Unir.RelatosdePapel.User.controller.dto.UserDto;
import com.Unir.RelatosdePapel.User.repository.users.UserJpaRepository;
import com.Unir.RelatosdePapel.User.repository.users.model.User;
import com.Unir.RelatosdePapel.User.utils.JwtUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserJpaRepository userRepository;
    private final JwtUtils jwtUtils;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserProfile(@PathVariable Integer userId,
                                          @RequestHeader("accessToken") String token) {

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token de autorización requerido");
        }

        
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o expirado");
        }

        Integer tokenUserId = jwtUtils.getUserIdFromToken(token);
        
        if (!tokenUserId.equals(userId)) {
            log.warn("Intento de acceso no autorizado al perfil de usuario: {}", userId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No autorizado para acceder a este perfil");
        }

        
        Optional<User> userOptional = userRepository.findById(Integer.valueOf(userId));
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOptional.get();
        UserDto userDto = new UserDto(
            user.getName(),
            user.getEmail(),
            user.getPhone(),
            user.getAddress(),
            user.getCif()            
        );
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUserProfile(@RequestHeader("accessToken") String token) {

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token de autorización requerido");
        }

        
        if (!jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o expirado");
        }

        Integer tokenUserId = jwtUtils.getUserIdFromToken(token);
        
        Optional<User> userOptional = userRepository.findById(tokenUserId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOptional.get();
        UserDto userDto = new UserDto(
            user.getName(),
            user.getEmail(),
            user.getPhone(),
            user.getAddress(),
            user.getCif()            
        );
        return ResponseEntity.ok(userDto);
    }
}
