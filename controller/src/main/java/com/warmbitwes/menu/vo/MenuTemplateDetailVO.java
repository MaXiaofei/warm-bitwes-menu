package com.warmbitwes.menu.vo;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "菜单模板详情")
public class MenuTemplateDetailVO {
    @Schema(description = "模板ID", example = "1")
    private Long id;
    @Schema(description = "模板名称", example = "工作日晚餐模板")
    private String name;
    @Schema(description = "模板类型（1-4）", example = "1")
    private Integer templateType;
    @Schema(description = "做饭场景", example = "工作日晚餐")
    private String scene;
    @Schema(description = "口味偏好", example = "微辣")
    private String flavor;
    @Schema(description = "适用人群", example = "2人")
    private String crowd;
    @Schema(description = "模板描述", example = "30分钟快手菜")
    private String description;
    @Schema(description = "状态（1启用 0停用）", example = "1")
    private Integer status;
    @Schema(description = "备注", example = "周内常用")
    private String remark;
    @Schema(description = "创建时间", example = "2026-04-24T10:00:00")
    private LocalDateTime createdAt;
    @Schema(description = "更新时间", example = "2026-04-24T11:00:00")
    private LocalDateTime updatedAt;
    @Schema(description = "关联菜品ID列表", example = "[1001,1002]")
    private List<Long> dishIds = new ArrayList<>();
}

