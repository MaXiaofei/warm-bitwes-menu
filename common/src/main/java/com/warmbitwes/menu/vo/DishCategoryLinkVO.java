package com.warmbitwes.menu.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 菜品与分类的关联视角：包含关联上的排序与备注。
 */
@Data
@NoArgsConstructor
public class DishCategoryLinkVO {
    private Long categoryId;
    private String categoryName;
    private Integer sortOrder;
    private String remark;
}
