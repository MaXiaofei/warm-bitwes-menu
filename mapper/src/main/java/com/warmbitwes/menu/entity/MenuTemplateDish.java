package com.warmbitwes.menu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 模板-菜品关联实体（menu_template_dish）。
 */
@Data
@NoArgsConstructor
public class MenuTemplateDish {
    private Long templateId;
    private Long dishId;
    private Integer sortOrder;
}

