package com.warmbitwes.menu.mapper;

import com.warmbitwes.menu.entity.MenuTemplate;
import com.warmbitwes.menu.entity.MenuTemplateDish;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 菜单模板 Mapper。
 */
@Mapper
public interface MenuTemplateMapper {

    /**
     * 新增菜单模板。
     *
     * @param template 模板
     * @return 影响行数
     */
    int insert(MenuTemplate template);

    /**
     * 批量新增模板-菜品关联。
     *
     * @param items 关联项
     * @return 影响行数
     */
    int batchInsertTemplateDishes(@Param("items") List<MenuTemplateDish> items);

    /**
     * 模板分页查询（按创建时间倒序，名称模糊可选）。
     *
     * @param keyword 名称关键字（可空）
     * @param offset 偏移量（从0开始）
     * @param limit 每页大小
     * @return 模板列表
     */
    List<MenuTemplate> selectPage(@Param("keyword") String keyword, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 模板分页总数（与 selectPage 筛选一致）。
     *
     * @param keyword 名称关键字（可空）
     * @return 总数
     */
    long countPage(@Param("keyword") String keyword);

    /**
     * 更新模板基础字段。
     *
     * @param template 模板
     * @return 影响行数
     */
    int updateById(MenuTemplate template);

    /**
     * 删除模板下全部菜品关联。
     *
     * @param templateId 模板ID
     * @return 影响行数
     */
    int deleteTemplateDishesByTemplateId(@Param("templateId") Long templateId);

    /**
     * 按 ID 查询模板。
     *
     * @param id 模板ID
     * @return 模板
     */
    MenuTemplate selectById(@Param("id") Long id);

    /**
     * 查询模板关联菜品ID列表。
     *
     * @param templateId 模板ID
     * @return 菜品ID列表
     */
    List<Long> selectDishIdsByTemplateId(@Param("templateId") Long templateId);

    /**
     * 查询模板的食材汇总。
     *
     * @param templateId 模板ID
     * @return 汇总行（ingredientId/ingredientName/unit/totalAmountG）
     */
    List<Map<String, Object>> selectIngredientSummaryByTemplateId(@Param("templateId") Long templateId);
}

