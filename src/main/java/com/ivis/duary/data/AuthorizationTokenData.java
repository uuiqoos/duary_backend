package com.ivis.duary.data;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthorizationTokenData {

    private String accessToken;

    private Long id;

    private String refreshToken;
}
