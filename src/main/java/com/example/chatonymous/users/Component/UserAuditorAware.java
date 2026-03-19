package com.example.chatonymous.users.Component;

import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Component
public class UserAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        String userName = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        return Optional.of(userName);
    }
}
