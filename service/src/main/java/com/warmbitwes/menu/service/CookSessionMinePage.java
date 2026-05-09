package com.warmbitwes.menu.service;

import com.warmbitwes.menu.vo.CookSessionMineItemVO;
import java.util.List;

/**
 * 我的做饭会话分页结果。
 *
 * @param records 当前页
 * @param total 总条数
 */
public record CookSessionMinePage(List<CookSessionMineItemVO> records, long total) {
}
