package com.Unir.RelatosdePapel.User.repository.sessions;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.Unir.RelatosdePapel.User.repository.sessions.model.RedisSessionData;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisSessionRepository {
    private static final String SESSION_KEY_PREFIX = "session:";
    private static final long TTL_SECONDS = 300; 

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public void saveSession(RedisSessionData redisSessionData) {
        try {
            String key = SESSION_KEY_PREFIX + redisSessionData.getSessionId();
            String jsonValue = objectMapper.writeValueAsString(redisSessionData);
            redisTemplate.opsForValue().set(key, jsonValue, TTL_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar sesión en redis", e);
        }
    }

    public Optional<RedisSessionData> findSession(String sessionId) {
        try {
            String key = SESSION_KEY_PREFIX + sessionId;
            String value = redisTemplate.opsForValue().get(key);
            if (value != null) {
                RedisSessionData redisSessionData = objectMapper.readValue(value.toString(), RedisSessionData.class);
                return Optional.of(redisSessionData);
            }
            return Optional.empty();
        } catch (Exception e) {
            deleteSession(sessionId);
            return Optional.empty();
        }
    }

    public void deleteSession(String sessionId) {
        String key = SESSION_KEY_PREFIX + sessionId;
        redisTemplate.delete(key);
    }
}