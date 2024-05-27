package com.ivis.duary.auth;

import com.ivis.duary.config.exception.CustomException;
import com.ivis.duary.config.response.ResponseCode;
import com.ivis.duary.data.AuthorizationTokenData;
import com.ivis.duary.model.Member;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AuthorizationTokenProvider {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";

    private static final long ACCESS_TOKEN_EXPIRE_TIME  = 1000L * 60 * 60 * 24 * 7;    // 7일
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 30;   // 30일

    private final Key key;

    public AuthorizationTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public AuthorizationTokenData generateAuthorizationTokenData(Member authentication) {

        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(String.valueOf(authentication.getId()))                     // payload "sub": "{id}"
                .claim(AUTHORITIES_KEY, authentication.getRole().getValue())            // payload "auth": "ROLE_USER"
                .setExpiration(accessTokenExpiresIn)                                    // payload "exp": 1516239022
                .signWith(key, SignatureAlgorithm.HS512)                                // header "alg": "HS512"
                .compact();

        // Refresh Token 생성
        Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);
        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return AuthorizationTokenData.builder()
                .accessToken(accessToken)
                .id(authentication.getId())
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        Long memberId = Long.valueOf(claims.getSubject());

        return new UsernamePasswordAuthenticationToken(memberId, null, authorities);
    }

    public Long getSubject(String accessToken) {
        Claims claims = parseClaims(accessToken);
        String subject = claims.getSubject();
        if (subject == null) {
            throw new CustomException(ResponseCode.UNAUTHORIZED);
        }
        return Long.valueOf(subject);
    }

    public void validateAuthorizationToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
            throw e;
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            throw e;
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
            throw e;
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
            throw e;
        }
    }

    public String getAuthorizationToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        try {
            return authorizationHeader.substring(7);
        } catch (Exception e) {
            return null;
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
