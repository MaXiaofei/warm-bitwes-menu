package com.warmbitwes.menu.mapper;

import com.warmbitwes.menu.entity.SessionRetrospective;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 会话复盘 Mapper。
 */
@Mapper
public interface SessionRetrospectiveMapper {

    /**
     * 按会话查询复盘（最多一行）。
     *
     * @param sessionId 会话ID
     * @return 复盘或 null
     */
    SessionRetrospective selectBySessionId(@Param("sessionId") Long sessionId);

    /**
     * 插入或更新复盘（按 session_id 唯一约束 UPSERT）。
     *
     * @param row 复盘
     * @return 影响行数
     */
    int upsert(SessionRetrospective row);
}
