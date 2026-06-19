package com.Unir.RelatosdePapel.User.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.Unir.RelatosdePapel.User.repository.sessions.RedisSessionRepository;
import com.Unir.RelatosdePapel.User.repository.sessions.model.RedisSessionData;
import com.Unir.RelatosdePapel.User.repository.users.UserJpaRepository;
import com.Unir.RelatosdePapel.User.repository.users.model.User;
import com.Unir.RelatosdePapel.User.service.model.SessionStatus;
import com.Unir.RelatosdePapel.User.service.model.SessionValidationResponse;
import com.Unir.RelatosdePapel.User.utils.JwtUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserJpaRepository userRepository;
    private final RedisSessionRepository redisSessionRepository;
    private final JwtUtils jwtUtils;

    public Optional<String> createPhanthomToken(String username, String password) {
        Optional<User> userOptional = userRepository.findByEmail(username);
        if (userOptional.isEmpty()) {
            return Optional.empty();
        }
        User user = userOptional.get();
        if (!user.getPassword().equals(password)) {
            return Optional.empty();
        }
        
        String accessToken = jwtUtils.generateAccessToken(username, user.getId());

        String opaqueToken = UUID.randomUUID().toString();

        RedisSessionData sessionData = new RedisSessionData(opaqueToken, accessToken,username, user.getId());
        redisSessionRepository.saveSession(sessionData);

        return Optional.of(opaqueToken);        
    }

        public SessionValidationResponse validatePhantomToken(String opaqueToken) {
            Optional<RedisSessionData> sessionDataOptional = redisSessionRepository.findSession(opaqueToken);
            if (sessionDataOptional.isEmpty()) {
                return new SessionValidationResponse(SessionStatus.NOT_FOUND, null);
            }
            RedisSessionData sessionData = sessionDataOptional.get();
            if (jwtUtils.validateToken(sessionData.getAccessToken())){
                return new SessionValidationResponse(SessionStatus.VALID, sessionData);
            }
             
            redisSessionRepository.deleteSession(opaqueToken);
            return new SessionValidationResponse(SessionStatus.EXPIRED, null);
        }

        public Optional<String> refreshPhantomToken(String opaqueToken) {
            Optional<RedisSessionData> sessionDataOptional = redisSessionRepository.findSession(opaqueToken);
            if (sessionDataOptional.isEmpty()) {
                return Optional.empty();
            }
            RedisSessionData sessionData = sessionDataOptional.get();
            if (!jwtUtils.validateToken(sessionData.getAccessToken())) {
                redisSessionRepository.deleteSession(opaqueToken);
                return Optional.empty();
            }

            String newAccessToken = jwtUtils.generateAccessToken(sessionData.getUsername(), sessionData.getUserId());
            String newOpaqueToken = UUID.randomUUID().toString();
            redisSessionRepository.deleteSession(opaqueToken);
            RedisSessionData newSessionData = new RedisSessionData(newOpaqueToken, newAccessToken, sessionData.getUsername(), sessionData.getUserId());
            redisSessionRepository.saveSession(newSessionData);

            return Optional.of(newOpaqueToken);
        }
}
