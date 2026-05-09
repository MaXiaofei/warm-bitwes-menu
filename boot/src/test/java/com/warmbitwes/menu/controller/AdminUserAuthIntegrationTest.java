package com.warmbitwes.menu.controller;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.warmbitwes.menu.advice.GlobalExceptionHandler;
import com.warmbitwes.menu.security.AuthInterceptor;
import com.warmbitwes.menu.security.LoginUser;
import com.warmbitwes.menu.security.TokenService;
import com.warmbitwes.menu.service.AdminUserService;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class AdminUserAuthIntegrationTest {
    private final AdminUserService adminUserService = mock(AdminUserService.class);
    private final TokenService tokenService = new TokenService();
    private final MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new AdminUserController(adminUserService))
            .setControllerAdvice(new GlobalExceptionHandler())
            .addInterceptors(new AuthInterceptor(tokenService))
            .build();

    @Test
    void should_reject_admin_api_when_mini_token_used() throws Exception {
        String token = tokenService.issueToken(new LoginUser(
                1L,
                "mini-user",
                "mini",
                Set.of("auth-user:create")
        ));

        mockMvc.perform(post("/api/admin/users")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"ops","nickname":"运营","phone":"13800000000","email":"ops@example.com","password":"123456","roleIds":[1]}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(10003));
    }

    @Test
    void should_reject_admin_api_when_permission_missing() throws Exception {
        String token = tokenService.issueToken(new LoginUser(
                2L,
                "admin-user",
                "admin",
                Set.of("dish:view")
        ));
        given(adminUserService.create(anyString(), anyString(), anyString(), anyString(), anyList()))
                .willReturn(1L);

        mockMvc.perform(post("/api/admin/users")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"ops","nickname":"运营","phone":"13800000000","email":"ops@example.com","password":"123456","roleIds":[1]}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(10003));
    }

    @Test
    void should_allow_admin_api_when_scope_and_permission_match() throws Exception {
        String token = tokenService.issueToken(new LoginUser(
                3L,
                "admin-user",
                "admin",
                Set.of("auth-user:create")
        ));
        given(adminUserService.create(anyString(), anyString(), anyString(), anyString(), anyList()))
                .willReturn(123L);

        mockMvc.perform(post("/api/admin/users")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"ops","nickname":"运营","phone":"13800000000","email":"ops@example.com","password":"123456","roleIds":[1]}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.id").value(123L));
    }
}
