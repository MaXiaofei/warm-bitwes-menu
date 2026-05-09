package com.warmbitwes.menu.mapper;

import com.warmbitwes.menu.entity.CookingSession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 做饭会话 Mapper。
 */
@Mapper
public interface CookingSessionMapper {

    /**
     * 新增做饭会话。
     *
     * @param session 会话
     * @return 影响行数
     */
    int insert(CookingSession session);

    /**
     * 按主键查询会话。
     *
     * @param id 会话ID
     * @return 会话
     */
    CookingSession selectById(@Param("id") Long id);
}

