package com.warmbitwes.menu.controller;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.warmbitwes.menu.service.AdminUserService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.mock;

class AdminUserControllerTest {
    private final AdminUserService adminUserService = mock(AdminUserService.class);
    private final MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new AdminUserController(adminUserService))
            .build();

    @Test
    void should_create_user() throws Exception {
        given(adminUserService.create(anyString(), anyString(), anyString(), anyString(), anyList()))
                .willReturn(1L);

        mockMvc.perform(post("/api/admin/users")
                        .header("Authorization", "Bearer any-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"ops","nickname":"运营","phone":"13800000000","email":"ops@example.com","password":"123456","roleIds":[1]}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.id").value(1L));
    }
}
