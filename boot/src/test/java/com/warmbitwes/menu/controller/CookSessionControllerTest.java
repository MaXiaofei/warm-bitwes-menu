package com.warmbitwes.menu.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.warmbitwes.menu.advice.GlobalExceptionHandler;
import com.warmbitwes.menu.exception.BizException;
import com.warmbitwes.menu.security.AuthInterceptor;
import com.warmbitwes.menu.security.LoginUser;
import com.warmbitwes.menu.security.TokenService;
import com.warmbitwes.menu.service.CookSessionService;
import com.warmbitwes.menu.vo.CookEventVO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * 做饭会话接口 Web 层测试（鉴权 + 参数错误码）。
 */
class CookSessionControllerTest {

    @Test
    void create_returns_biz_code_when_neither_template_nor_dishes() throws Exception {
        CookSessionService svc = Mockito.mock(CookSessionService.class);
        given(svc.create(isNull(), eq(List.of()), any(LocalDateTime.class)))
                .willThrow(new BizException(10013, "请选择菜单模板或自选菜品"));

        TokenService tokenService = new TokenService();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new CookSessionController(svc))
                .setControllerAdvice(new GlobalExceptionHandler())
                .addInterceptors(new AuthInterceptor(tokenService))
                .build();

        String token = tokenService.issueToken(new LoginUser(
                1L,
                "mini-user",
                "mini",
                Set.of("cook-session:create")
        ));

        mockMvc.perform(post("/api/cook-sessions")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"startedAt\":\"2026-05-09T12:00:00\",\"dishIds\":[]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(10013));
    }

    @Test
    void list_events_returns_empty_array() throws Exception {
        CookSessionService svc = Mockito.mock(CookSessionService.class);
        given(svc.listEventsForCurrentUser(9L)).willReturn(List.of());

        TokenService tokenService = new TokenService();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new CookSessionController(svc))
                .setControllerAdvice(new GlobalExceptionHandler())
                .addInterceptors(new AuthInterceptor(tokenService))
                .build();

        String token = tokenService.issueToken(new LoginUser(
                1L,
                "mini-user",
                "mini",
                Set.of("cook-session:event-read")
        ));

        mockMvc.perform(get("/api/cook-sessions/9/events")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.length()").value(0));
    }

    @Test
    void list_events_returns_one_item_after_post_flow() throws Exception {
        CookSessionService svc = Mockito.mock(CookSessionService.class);
        CookEventVO vo = new CookEventVO();
        vo.setId(1L);
        vo.setSessionId(3L);
        vo.setEventType("STEP");
        vo.setEventTime(LocalDateTime.parse("2026-05-09T12:00:00"));
        vo.setContent("切菜");
        given(svc.listEventsForCurrentUser(3L)).willReturn(List.of(vo));

        TokenService tokenService = new TokenService();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new CookSessionController(svc))
                .setControllerAdvice(new GlobalExceptionHandler())
                .addInterceptors(new AuthInterceptor(tokenService))
                .build();

        String token = tokenService.issueToken(new LoginUser(
                1L,
                "mini-user",
                "mini",
                Set.of("cook-session:event-read")
        ));

        mockMvc.perform(get("/api/cook-sessions/3/events")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].eventType").value("STEP"));
    }
}
