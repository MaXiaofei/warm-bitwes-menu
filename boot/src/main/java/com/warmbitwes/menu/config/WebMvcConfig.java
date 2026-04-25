package com.warmbitwes.menu.config;

import com.warmbitwes.menu.security.AuthInterceptor;
import com.warmbitwes.menu.security.TokenService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final TokenService tokenService;

    public WebMvcConfig(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor(tokenService))
                .addPathPatterns("/api/admin/**");
    }
}
