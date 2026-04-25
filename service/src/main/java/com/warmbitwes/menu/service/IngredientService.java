package com.warmbitwes.menu.service;

import com.warmbitwes.menu.entity.Ingredient;
import com.warmbitwes.menu.exception.BizException;
import com.warmbitwes.menu.mapper.IngredientMapper;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 食材领域服务：承载食材相关业务逻辑。
 */
@Service
public class IngredientService {

    private final IngredientMapper ingredientMapper;

    public IngredientService(IngredientMapper ingredientMapper) {
        this.ingredientMapper = ingredientMapper;
    }

    /**
     * 查询全量食材列表。
     *
     * @return 全量列表
     */
    public List<Ingredient> listAll() {
        return ingredientMapper.selectAll();
    }

    /**
     * 食材分页列表。
     *
     * @param offset 偏移量（从0开始）
     * @param limit 每页大小
     * @return 列表
     */
    public List<Ingredient> listPage(int offset, int limit) {
        return ingredientMapper.selectPage(offset, limit);
    }

    /**
     * 按 ID 查询食材。
     *
     * @param id 食材ID
     * @return 食材
     */
    public Ingredient getById(Long id) {
        Ingredient ingredient = ingredientMapper.selectById(id);
        if (ingredient == null) {
            throw new BizException(40401, "食材不存在，id=" + id);
        }
        return ingredient;
    }

    /**
     * 新增食材。
     *
     * @param ingredient 食材
     */
    public void create(Ingredient ingredient) {
        ingredientMapper.insert(ingredient);
    }

    /**
     * 新增食材并返回自增主键。
     *
     * @param ingredient 食材
     * @return 新建 id
     */
    public Long createAndReturnId(Ingredient ingredient) {
        if (ingredientMapper.countByName(ingredient.getName()) > 0) {
            throw new BizException(10021, "食材名称已存在");
        }
        ingredientMapper.insert(ingredient);
        return ingredient.getId();
    }

    /**
     * 按 ID 更新食材。
     *
     * @param id 食材ID
     * @param payload 更新内容
     */
    public void updateById(Long id, Ingredient payload) {
        Ingredient ingredient = ingredientMapper.selectById(id);
        if (ingredient == null) {
            throw new BizException(40401, "食材不存在，id=" + id);
        }

        ingredient.setName(payload.getName());
        ingredient.setUnit(payload.getUnit());
        ingredient.setCaloriesKcalPer100g(payload.getCaloriesKcalPer100g());
        ingredient.setGiValue(payload.getGiValue());
        ingredient.setRemark(payload.getRemark());
        ingredientMapper.updateById(ingredient);
    }

    /**
     * 按 ID 删除食材。
     *
     * @param id 食材ID
     */
    public void deleteById(Long id) {
        int rows = ingredientMapper.deleteById(id);
        if (rows <= 0) {
            throw new BizException(40401, "食材不存在，id=" + id);
        }
    }
}
