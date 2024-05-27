package com.ivis.duary.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String systemAuditing = "0";

        // not logged in
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.of(systemAuditing);
        }
        // logged in
        try {
            Long memberId = (Long) authentication.getPrincipal();

            return Optional.of(memberId.toString());
        }
        // not logged in
        catch (ClassCastException e) {
//            String anonymousUser = (String) authentication.getPrincipal();
            return Optional.of(systemAuditing);
        }
    }
}