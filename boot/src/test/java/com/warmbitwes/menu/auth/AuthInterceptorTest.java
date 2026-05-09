package com.warmbitwes.menu.auth;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.warmbitwes.menu.exception.BizException;
import com.warmbitwes.menu.security.AuthInterceptor;
import com.warmbitwes.menu.security.LoginUser;
import com.warmbitwes.menu.security.TokenService;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;

class AuthInterceptorTest {
    @Test
    void should_reject_request_without_permission() throws Exception {
        TokenService tokenService = new TokenService();
        String token = tokenService.issueToken(new LoginUser(
                1L,
                "mini-user",
                "mini",
                Set.of("auth-user:create")
        ));
        AuthInterceptor interceptor = new AuthInterceptor(tokenService);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/admin/users");
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerMethod handlerMethod = new HandlerMethod(
                new ProbeController(),
                ProbeController.class.getDeclaredMethod("create")
        );

        BizException ex = assertThrows(BizException.class, () -> interceptor.preHandle(request, response, handlerMethod));
        assertEquals(10003, ex.getCode());
    }

    @Test
    void should_reject_when_authorization_header_missing() throws Exception {
        AuthInterceptor interceptor = new AuthInterceptor(new TokenService());
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/admin/users");
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerMethod handlerMethod = new HandlerMethod(
                new ProbeController(),
                ProbeController.class.getDeclaredMethod("create")
        );

        BizException ex = assertThrows(BizException.class, () -> interceptor.preHandle(request, response, handlerMethod));
        assertEquals(10002, ex.getCode());
    }

    @Test
    void should_allow_when_scope_and_permission_match() throws Exception {
        TokenService tokenService = new TokenService();
        String token = tokenService.issueToken(new LoginUser(
                2L,
                "admin-user",
                "admin",
                Set.of("auth-user:create")
        ));
        AuthInterceptor interceptor = new AuthInterceptor(tokenService);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/admin/users");
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        HandlerMethod handlerMethod = new HandlerMethod(
                new ProbeController(),
                ProbeController.class.getDeclaredMethod("create")
        );

        boolean allowed = interceptor.preHandle(request, response, handlerMethod);
        assertTrue(allowed);
    }

    static class ProbeController {
        @com.warmbitwes.menu.security.RequirePermission("auth-user:create")
        public void create() {
        }
    }
}
