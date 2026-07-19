package com.acadance.app.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUser {
    public static Long id() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
