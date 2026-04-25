package com.warmbitwes.menu.mapper;

import com.warmbitwes.menu.entity.Ingredient;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IngredientMapper {

    /**
     * 查询全量食材列表。
     *
     * @return 列表
     */
    List<Ingredient> selectAll();

    /**
     * 食材分页查询。
     *
     * @param offset 偏移量（从0开始）
     * @param limit 每页大小
     * @return 列表
     */
    List<Ingredient> selectPage(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 按主键查询食材。
     *
     * @param id 食材ID
     * @return 食材；不存在返回 null
     */
    Ingredient selectById(@Param("id") Long id);

    /**
     * 新增食材（useGeneratedKeys 回填 id）。
     *
     * @param ingredient 食材
     * @return 影响行数
     */
    int insert(Ingredient ingredient);

    /**
     * 按主键更新食材。
     *
     * @param ingredient 食材
     * @return 影响行数
     */
    int updateById(Ingredient ingredient);

    /**
     * 按主键删除食材。
     *
     * @param id 食材ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 按名称查询数量。
     *
     * @param name 名称
     * @return 数量
     */
    int countByName(@Param("name") String name);
}
