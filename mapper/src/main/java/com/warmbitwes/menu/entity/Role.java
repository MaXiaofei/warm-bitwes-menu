package com.warmbitwes.menu.entity;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Role {
    private Long id;
    private String name;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
