package com.warmbitwes.menu.service;

import com.warmbitwes.menu.entity.Dish;
import com.warmbitwes.menu.entity.DishDetail;
import com.warmbitwes.menu.entity.DishIngredient;
import com.warmbitwes.menu.entity.Ingredient;
import com.warmbitwes.menu.exception.BizException;
import com.warmbitwes.menu.mapper.DishMapper;
import com.warmbitwes.menu.mapper.IngredientMapper;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 菜品领域服务：承载菜品相关业务逻辑。
 */
@Service
public class DishService {

    private final DishMapper dishMapper;
    private final IngredientMapper ingredientMapper;

    public DishService(DishMapper dishMapper, IngredientMapper ingredientMapper) {
        this.dishMapper = dishMapper;
        this.ingredientMapper = ingredientMapper;
    }

    /**
     * 查询菜品详情。
     *
     * @param id 菜品ID
     * @return 详情
     */
    public DishDetail getDetailById(Long id) {
        DishDetail dish = dishMapper.selectById(id);
        if (dish == null) {
            throw new BizException(40402, "菜品不存在，id=" + id);
        }
        return dish;
    }

    /**
     * 新增菜品。
     *
     * @param dish 菜品
     * @return 新建 id
     */
    public Long create(Dish dish) {
        if (dish.getStatus() == null) {
            dish.setStatus(1);
        }
        dishMapper.insert(dish);
        return dish.getId();
    }

    /**
     * 菜品分页列表。
     *
     * @param pageNum 页码（从1开始）
     * @param pageSize 每页大小（最大100）
     * @return 列表
     */
    public List<Dish> listPage(int pageNum, int pageSize) {
        int safePageNum = Math.max(1, pageNum);
        int safePageSize = Math.min(100, Math.max(1, pageSize));
        int offset = (safePageNum - 1) * safePageSize;
        return dishMapper.selectPage(offset, safePageSize);
    }

    /**
     * 全量替换菜品食材关联。
     *
     * @param dishId 菜品ID
     * @param items 关联项
     */
    public void replaceIngredients(Long dishId, List<DishIngredient> items) {
        DishDetail dish = dishMapper.selectById(dishId);
        if (dish == null) {
            throw new BizException(40402, "菜品不存在，id=" + dishId);
        }
        for (DishIngredient item : items) {
            Ingredient ingredient = ingredientMapper.selectById(item.getIngredientId());
            if (ingredient == null) {
                throw new BizException(40401, "食材不存在，id=" + item.getIngredientId());
            }
            item.setDishId(dishId);
        }

        dishMapper.deleteIngredientsByDishId(dishId);
        if (!items.isEmpty()) {
            dishMapper.batchInsertDishIngredients(items);
        }
    }
}
