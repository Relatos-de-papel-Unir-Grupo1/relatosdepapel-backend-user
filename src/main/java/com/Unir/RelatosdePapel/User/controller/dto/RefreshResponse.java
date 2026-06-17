package com.Unir.RelatosdePapel.User.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshResponse {
    private String newSessionId;
    private String accessToken;
}
