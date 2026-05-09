package com.warmbitwes.menu.mapper;

import com.warmbitwes.menu.entity.PrepItem;
import com.warmbitwes.menu.entity.SessionDish;
import com.warmbitwes.menu.entity.SessionReview;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * P2 备菜与点评相关 Mapper。
 */
@Mapper
public interface P2Mapper {

    /**
     * 删除会话下已有备菜项。
     *
     * @param sessionId 会话ID
     * @return 影响行数
     */
    int deletePrepItemsBySessionId(@Param("sessionId") Long sessionId);

    /**
     * 批量新增备菜项。
     *
     * @param items 备菜项
     * @return 影响行数
     */
    int batchInsertPrepItems(@Param("items") List<PrepItem> items);

    /**
     * 查询会话备菜清单（含食材名与单位）。
     *
     * @param sessionId 会话ID
     * @return 清单行
     */
    List<Map<String, Object>> selectPrepItemsBySessionId(@Param("sessionId") Long sessionId);

    /**
     * 新增会话口味点评。
     *
     * @param review 点评
     * @return 影响行数
     */
    int insertSessionReview(SessionReview review);

    /**
     * 查询会话点评列表。
     *
     * @param sessionId 会话ID
     * @return 点评行
     */
    List<Map<String, Object>> selectReviewsBySessionId(@Param("sessionId") Long sessionId);

    /**
     * 批量写入会话-菜品（自选菜单）。
     *
     * @param rows 行列表
     * @return 影响行数
     */
    int batchInsertSessionDishes(@Param("rows") List<SessionDish> rows);
}

