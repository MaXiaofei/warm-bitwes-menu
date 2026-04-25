package com.warmbitwes.menu.service;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AdminPermissionService {
    public List<String> listAllCodes() {
        return List.of(
                "auth-user:list",
                "auth-user:create",
                "auth-role:grant",
                "dish:create",
                "dish:update"
        );
    }
}
