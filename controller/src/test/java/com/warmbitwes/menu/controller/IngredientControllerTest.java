package com.warmbitwes.menu.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.warmbitwes.menu.service.IngredientService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.mock;

class IngredientControllerTest {
    private final IngredientService ingredientService = mock(IngredientService.class);
    private final MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new IngredientController(ingredientService))
            .build();

    @Test
    void should_create_ingredient() throws Exception {
        given(ingredientService.createAndReturnId(org.mockito.ArgumentMatchers.any())).willReturn(1L);

        mockMvc.perform(post("/api/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"鸡蛋","unit":"个","caloriesKcalPer100g":143,"giValue":30}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.id").value(1L));
    }
}
