package com.warmbitwes.menu.mapper;

import com.warmbitwes.menu.entity.CookEvent;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 烹饪过程事件 Mapper。
 */
@Mapper
public interface CookEventMapper {

    /**
     * 新增一条事件。
     *
     * @param row 事件
     * @return 影响行数
     */
    int insert(CookEvent row);

    /**
     * 按会话查询事件，按发生时间升序。
     *
     * @param sessionId 会话ID
     * @return 事件列表
     */
    List<CookEvent> selectBySessionIdOrderByEventTimeAsc(@Param("sessionId") Long sessionId);
}
