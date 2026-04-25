package com.warmbitwes.menu.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class AdminUserService {
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final Map<Long, AdminUserItem> userStore = new LinkedHashMap<>();

    public List<AdminUserItem> list() {
        return new ArrayList<>(userStore.values());
    }

    public Long create(String username, String nickname, String phone, String email, List<Long> roleIds) {
        long id = idGenerator.getAndIncrement();
        userStore.put(id, new AdminUserItem(id, username, nickname, phone, email, 1, roleIds));
        return id;
    }

    public void update(Long id, String nickname, String phone, String email, Integer status, List<Long> roleIds) {
        AdminUserItem previous = userStore.get(id);
        if (previous == null) {
            return;
        }
        int safeStatus = status == null ? previous.status() : status;
        userStore.put(id, new AdminUserItem(id, previous.username(), nickname, phone, email, safeStatus, roleIds));
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
