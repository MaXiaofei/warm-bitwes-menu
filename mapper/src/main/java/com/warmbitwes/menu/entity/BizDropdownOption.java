package com.warmbitwes.menu.entity;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 下拉选项配置（biz_dropdown_option）。
 */
@Data
@NoArgsConstructor
public class BizDropdownOption {

    private Long id;
    private String category;
    private String optionCode;
    private String optionLabel;
    private Integer sortOrder;
    private Integer enabled;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
