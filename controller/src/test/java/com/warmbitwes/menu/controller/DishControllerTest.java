package com.warmbitwes.menu.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.warmbitwes.menu.service.DishService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.mock;

class DishControllerTest {
    private final DishService dishService = mock(DishService.class);
    private final MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new DishController(dishService))
            .build();

    @Test
    void should_update_dish_status() throws Exception {
        mockMvc.perform(put("/api/dishes/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"status":1}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0));
    }
}
