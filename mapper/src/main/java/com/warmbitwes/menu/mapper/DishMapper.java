package com.warmbitwes.menu.mapper;

import com.warmbitwes.menu.entity.Dish;
import com.warmbitwes.menu.entity.DishDetail;
import com.warmbitwes.menu.entity.DishIngredient;
import com.warmbitwes.menu.dto.DishUpdateReq;
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
    List<Dish> selectPage(@Param("name") String name,
                          @Param("status") Integer status,
                          @Param("offset") int offset,
                          @Param("limit") int limit);

    /**
     * 删除指定菜品的全部食材关联。
     *
     * @param dishId 菜品ID
     * @return 影响行数
     */
    int deleteIngredientsByDishId(@Param("dishId") Long dishId);

    /**
     * 查询菜品食材关联列表。
     *
     * @param dishId 菜品ID
     * @return 关联列表
     */
    List<DishIngredient> selectIngredientsByDishId(@Param("dishId") Long dishId);

    /**
     * 批量新增菜品食材关联。
     *
     * @param items 关联项
     * @return 影响行数
     */
    int batchInsertDishIngredients(@Param("items") List<DishIngredient> items);

    /**
     * 更新菜品基础字段（动态更新，过滤软删）。
     *
     * @param id 菜品ID
     * @param req 更新请求
     * @return 影响行数
     */
    int updateDishBase(@Param("id") Long id, @Param("req") DishUpdateReq req);

    /**
     * 更新菜品上下架状态（过滤软删）。
     *
     * @param id 菜品ID
     * @param status 状态：1上架 0下架
     * @return 影响行数
     */
    int updateDishStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 软删除菜品（is_deleted=1）。
     *
     * @param id 菜品ID
     * @return 影响行数
     */
    int softDelete(@Param("id") Long id);
}
