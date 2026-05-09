package com.warmbitwes.menu.vo;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 管理端：下拉选项完整信息。
 */
@Data
@NoArgsConstructor
public class DropdownOptionAdminVO {

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
