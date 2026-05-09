package com.warmbitwes.menu.mapper;

import com.warmbitwes.menu.entity.BizDropdownOption;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 下拉选项配置 Mapper。
 */
@Mapper
public interface BizDropdownOptionMapper {

    /**
     * 按分类查询已启用选项（下拉用）。
     *
     * @param category 分类编码
     * @return 列表
     */
    List<BizDropdownOption> selectEnabledByCategory(@Param("category") String category);

    /**
     * 已启用选项涉及的全部分类编码。
     *
     * @return 分类编码列表
     */
    List<String> selectDistinctCategories();

    /**
     * 管理端按分类筛选（为空则返回全部）。
     *
     * @param category 分类，可空
     * @return 列表
     */
    List<BizDropdownOption> selectForAdmin(@Param("category") String category);

    /**
     * 主键查询。
     *
     * @param id 主键
     * @return 记录或 null
     */
    BizDropdownOption selectById(@Param("id") Long id);

    /**
     * 插入。
     *
     * @param row 实体
     * @return 影响行数
     */
    int insert(BizDropdownOption row);

    /**
     * 按主键更新。
     *
     * @param row 实体（须含 id）
     * @return 影响行数
     */
    int updateById(BizDropdownOption row);

    /**
     * 按主键删除。
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
}
