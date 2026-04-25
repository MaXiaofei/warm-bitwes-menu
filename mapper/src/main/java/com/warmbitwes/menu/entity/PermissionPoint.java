package com.warmbitwes.menu.entity;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PermissionPoint {
    private Long id;
    private String code;
    private String name;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
