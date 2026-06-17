package com.Unir.RelatosdePapel.User.service.model;

import com.Unir.RelatosdePapel.User.repository.sessions.model.RedisSessionData;

public record SessionValidationResponse(SessionStatus status, RedisSessionData redisSessionData) {

}
