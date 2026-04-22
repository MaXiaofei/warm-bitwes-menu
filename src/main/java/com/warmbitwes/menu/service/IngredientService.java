package com.warmbitwes.menu.service;

import com.warmbitwes.menu.dto.IngredientCreateReq;
import com.warmbitwes.menu.dto.IngredientUpdateReq;
import com.warmbitwes.menu.entity.Ingredient;
import com.warmbitwes.menu.exception.BizException;
import com.warmbitwes.menu.mapper.IngredientMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {

    private final IngredientMapper ingredientMapper;

    public IngredientService(IngredientMapper ingredientMapper) {
        this.ingredientMapper = ingredientMapper;
    }

    public List<Ingredient> listAll() {
        return ingredientMapper.selectAll();
    }

    public Ingredient getById(Long id) {
        Ingredient ingredient = ingredientMapper.selectById(id);
        if (ingredient == null) {
            throw new BizException(40401, "食材不存在，id=" + id);
        }
        return ingredient;
    }

    public void create(IngredientCreateReq req) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(req.getName());
        ingredient.setUnit(req.getUnit());
        ingredient.setCaloriesKcalPer100g(req.getCaloriesKcalPer100g());
        ingredient.setGiValue(req.getGiValue());
        ingredient.setRemark(req.getRemark());
        ingredientMapper.insert(ingredient);
    }

    public void updateById(Long id, IngredientUpdateReq req) {
        Ingredient ingredient = ingredientMapper.selectById(id);
        if (ingredient == null) {
            throw new BizException(40401, "食材不存在，id=" + id);
        }

        ingredient.setName(req.getName());
        ingredient.setUnit(req.getUnit());
        ingredient.setCaloriesKcalPer100g(req.getCaloriesKcalPer100g());
        ingredient.setGiValue(req.getGiValue());
        ingredient.setRemark(req.getRemark());
        ingredientMapper.updateById(ingredient);
    }

    public void deleteById(Long id) {
        int rows = ingredientMapper.deleteById(id);
        if (rows <= 0) {
            throw new BizException(40401, "食材不存在，id=" + id);
        }
    }
}

