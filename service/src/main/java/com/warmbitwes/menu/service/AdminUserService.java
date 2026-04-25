package com.warmbitwes.menu.service;

import com.warmbitwes.menu.entity.AppUser;
import com.warmbitwes.menu.mapper.AppUserMapper;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AdminUserService {
    private static final String DEFAULT_RESET_PASSWORD_HASH = "reset_123456_hash";
    private final AppUserMapper appUserMapper;

    public AdminUserService(AppUserMapper appUserMapper) {
        this.appUserMapper = appUserMapper;
    }

    public List<AdminUserItem> list() {
        return appUserMapper.selectAdminUsers().stream()
                .map(item -> new AdminUserItem(
                        item.getId(),
                        item.getUsername(),
                        item.getNickname(),
                        item.getPhone(),
                        item.getRemark(),
                        item.getStatus(),
                        List.of(1L)))
                .toList();
    }

    public Long create(String username, String nickname, String phone, String email, List<Long> roleIds) {
        AppUser entity = new AppUser();
        entity.setUsername(username);
        entity.setPasswordHash("init_123456_hash");
        entity.setNickname(nickname);
        entity.setPhone(phone);
        entity.setStatus(1);
        entity.setRemark(email);
        appUserMapper.insertAdminUser(entity);
        return entity.getId();
    }

    public void update(Long id, String nickname, String phone, String email, Integer status, List<Long> roleIds) {
        AppUser entity = new AppUser();
        entity.setId(id);
        entity.setNickname(nickname);
        entity.setPhone(phone);
        entity.setStatus(status == null ? 1 : status);
        entity.setRemark(email);
        appUserMapper.updateAdminUserById(entity);
    }

    public void resetPassword(Long id) {
        appUserMapper.updatePasswordById(id, DEFAULT_RESET_PASSWORD_HASH);
    }

    public record AdminUserItem(
            Long id,
            String username,
            String nickname,
            String phone,
            String email,
            Integer status,
            List<Long> roleIds
    ) {
    }
}
