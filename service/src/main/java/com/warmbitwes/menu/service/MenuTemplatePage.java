package com.warmbitwes.menu.service;

import com.warmbitwes.menu.entity.MenuTemplate;
import java.util.List;

/**
 * 菜单模板分页查询结果。
 *
 * @param records 当前页
 * @param total 总条数
 */
public record MenuTemplatePage(List<MenuTemplate> records, long total) {
}
