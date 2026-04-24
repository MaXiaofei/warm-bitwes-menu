package com.warmbitwes.menu.vo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 菜单模板详情视图。
 */
@Data
@NoArgsConstructor
public class MenuTemplateDetailVO {
    private Long id;
    private String name;
    private Integer templateType;
    private String scene;
    private String flavor;
    private String crowd;
    private String description;
    private Integer status;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Long> dishIds = new ArrayList<>();
}

