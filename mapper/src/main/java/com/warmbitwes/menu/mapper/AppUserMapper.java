package com.warmbitwes.menu.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper（联调用最小能力）。
 */
@Mapper
public interface AppUserMapper {

    /**
     * 查询任意一个用户ID。
     *
     * @return 用户ID
     */
    Long selectAnyUserId();

    /**
     * 按用户名查询用户ID。
     *
     * @param username 用户名
     * @return 用户ID
     */
    Long selectIdByUsername(String username);

    /**
     * 新增联调用默认用户。
     *
     * @param username 用户名
     * @return 影响行数
     */
    int insertDevUser(String username);
}

