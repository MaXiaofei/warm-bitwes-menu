package com.warmbitwes.menu.mapper;

import com.warmbitwes.menu.entity.Dish;
import com.warmbitwes.menu.entity.DishDetail;
import com.warmbitwes.menu.entity.DishIngredient;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DishMapper {

    /**
     * 按主键查询菜品详情（含分类关联）。
     *
     * @param id 菜品ID
     * @return 详情；不存在返回 null
     */
    DishDetail selectById(@Param("id") Long id);

    /**
     * 新增菜品（useGeneratedKeys 回填 id）。
     *
     * @param dish 菜品
     * @return 影响行数
     */
    int insert(Dish dish);

    /**
     * 菜品分页查询（过滤软删）。
     *
     * @param offset 偏移量（从0开始）
     * @param limit 每页大小
     * @return 列表
     */
    List<Dish> selectPage(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 删除指定菜品的全部食材关联。
     *
     * @param dishId 菜品ID
     * @return 影响行数
     */
    int deleteIngredientsByDishId(@Param("dishId") Long dishId);

    /**
     * 批量新增菜品食材关联。
     *
     * @param items 关联项
     * @return 影响行数
     */
    int batchInsertDishIngredients(@Param("items") List<DishIngredient> items);
}
