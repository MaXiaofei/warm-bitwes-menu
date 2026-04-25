package com.warmbitwes.menu.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.warmbitwes.menu.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.mock;

class AuthControllerTest {
    private final AuthService authService = mock(AuthService.class);
    private final MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new AuthController(authService))
            .build();

    @Test
    void login_should_return_token_when_credentials_valid() throws Exception {
        given(authService.login("admin", "123456", "admin"))
                .willReturn(new AuthService.AuthLoginResult("access-token", "refresh-token", 7200));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"admin","password":"123456","clientType":"admin"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.accessToken").value("access-token"));
    }
}
