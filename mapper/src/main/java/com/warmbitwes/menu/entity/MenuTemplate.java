package com.warmbitwes.menu.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 菜单模板实体（menu_template）。
 */
@Data
@NoArgsConstructor
public class MenuTemplate {
    private Long id;
    private String name;
    private Integer templateType;
    private String scene;
    private String flavor;
    private String crowd;
    private String description;
    private Integer status;
    private Long createdBy;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

