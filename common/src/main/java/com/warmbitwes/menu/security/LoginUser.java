package com.warmbitwes.menu.security;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser {
    private Long userId;
    private String username;
    private String clientType;
    private Set<String> permissions;
}
