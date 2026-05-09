package com.warmbitwes.menu.vo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DishDetailVO {
    private Long id;
    private String name;
    private String coverUrl;
    private String steps;
    private String notes;
    private Integer durationMin;
    private Integer difficulty;
    private Integer status;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<DishCategoryLinkVO> categories = new ArrayList<>();
}
