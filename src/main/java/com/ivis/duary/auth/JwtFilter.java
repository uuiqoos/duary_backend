package com.ivis.duary.auth;

import com.ivis.duary.config.response.Response;
import com.ivis.duary.repository.TokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@SuppressWarnings("NullableProblems")
@RequiredArgsConstructor
@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final AuthorizationTokenProvider authorizationTokenProvider;

    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {

        String jwt = authorizationTokenProvider.getAuthorizationToken(request);

        ObjectMapper om = new ObjectMapper();
        Response<?> res = new Response<>(401, true, null);
        try {
            if (StringUtils.hasText(jwt)) {
                authorizationTokenProvider.validateAuthorizationToken(jwt);
                Authentication authentication = authorizationTokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        }
        catch (Exception e) {

            if (e instanceof JwtException) {
               res.setMessage("wrong jwt");
            } else {
                res.setMessage(e.toString());
            }
            response.getOutputStream().print(om.writeValueAsString(res));
        }
    }
}