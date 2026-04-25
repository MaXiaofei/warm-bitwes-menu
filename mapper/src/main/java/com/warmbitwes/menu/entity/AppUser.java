package com.warmbitwes.menu.entity;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class AppUser {
    private Long id;
    private String username;
    private String passwordHash;
    private String nickname;
    private String phone;
    private Integer status;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
