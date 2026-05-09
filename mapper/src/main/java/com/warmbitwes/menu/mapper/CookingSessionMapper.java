package com.warmbitwes.menu.mapper;

import com.warmbitwes.menu.entity.CookingSession;
import com.warmbitwes.menu.entity.CookingSessionMineRow;
import java.util.List;
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

    /**
     * 统计用户会话数量。
     *
     * @param userId 用户ID
     * @return 数量
     */
    long countByUserId(@Param("userId") Long userId);

    /**
     * 分页查询用户会话摘要（含模板名）。
     *
     * @param userId 用户ID
     * @param offset 偏移
     * @param limit 条数
     * @return 行列表
     */
    List<CookingSessionMineRow> selectMineByUserId(@Param("userId") Long userId,
                                                   @Param("offset") int offset,
                                                   @Param("limit") int limit);
}

