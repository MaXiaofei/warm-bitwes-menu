package com.warmbitwes.menu.service;

import com.warmbitwes.menu.mapper.PermissionPointMapper;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AdminPermissionService {
    private final PermissionPointMapper permissionPointMapper;

    public AdminPermissionService(PermissionPointMapper permissionPointMapper) {
        this.permissionPointMapper = permissionPointMapper;
    }

    public List<PermissionPointItem> listAll() {
        return permissionPointMapper.selectAll().stream()
                .map(item -> new PermissionPointItem(item.getId(), item.getCode(), item.getName()))
                .toList();
    }

    public record PermissionPointItem(Long id, String code, String name) {
    }
}
