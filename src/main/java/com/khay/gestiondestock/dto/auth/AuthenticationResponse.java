package com.khay.gestiondestock.dto.auth;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthenticationResponse {

    private String accessToken;
}
