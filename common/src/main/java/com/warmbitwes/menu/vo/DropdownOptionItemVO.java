package com.warmbitwes.menu.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 下拉框单项（optionCode ↔ optionLabel）。
 */
@Data
@NoArgsConstructor
public class DropdownOptionItemVO {

    private String code;

    private String label;

    private Integer sortOrder;
}
