package com.warmbitwes.menu.security;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class TokenService {
    private final Map<String, LoginUser> tokenStore = new ConcurrentHashMap<>();

    public String issueToken(LoginUser loginUser) {
        String token = UUID.randomUUID().toString();
        tokenStore.put(token, loginUser);
        return token;
    }

    public LoginUser parse(String token) {
        return tokenStore.get(token);
    }
}
