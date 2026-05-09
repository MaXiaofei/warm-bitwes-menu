package com.warmbitwes.menu.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.warmbitwes.menu.entity.Ingredient;
import com.warmbitwes.menu.exception.BizException;
import com.warmbitwes.menu.mapper.IngredientMapper;
import org.junit.jupiter.api.Test;

class IngredientServiceTest {
    private final IngredientMapper ingredientMapper = mock(IngredientMapper.class);
    private final IngredientService ingredientService = new IngredientService(ingredientMapper);

    @Test
    void create_should_throw_when_name_already_exists() {
        given(ingredientMapper.countByName("鸡蛋")).willReturn(1);
        Ingredient ingredient = new Ingredient();
        ingredient.setName("鸡蛋");

        BizException ex = assertThrows(BizException.class, () -> ingredientService.createAndReturnId(ingredient));
        assertEquals(10021, ex.getCode());
    }

    @Test
    void create_should_return_generated_id_when_name_unique() {
        given(ingredientMapper.countByName("鸡蛋")).willReturn(0);
        given(ingredientMapper.insert(any())).willAnswer(invocation -> {
            Ingredient arg = invocation.getArgument(0);
            arg.setId(99L);
            return 1;
        });
        Ingredient ingredient = new Ingredient();
        ingredient.setName("鸡蛋");

        Long id = ingredientService.createAndReturnId(ingredient);
        assertEquals(99L, id);
    }
}
