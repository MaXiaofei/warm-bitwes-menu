package com.warmbitwes.menu.service;

import com.warmbitwes.menu.dto.MenuTemplateUpdateReq;
import com.warmbitwes.menu.entity.DishDetail;
import com.warmbitwes.menu.entity.MenuTemplate;
import com.warmbitwes.menu.entity.MenuTemplateDish;
import com.warmbitwes.menu.exception.BizException;
import com.warmbitwes.menu.mapper.DishMapper;
import com.warmbitwes.menu.mapper.MenuTemplateMapper;
import com.warmbitwes.menu.vo.MenuTemplateIngredientSummaryVO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * 菜单模板领域服务。
 */
@Service
public class MenuTemplateService {

    private final MenuTemplateMapper menuTemplateMapper;
    private final DishMapper dishMapper;

    public MenuTemplateService(MenuTemplateMapper menuTemplateMapper, DishMapper dishMapper) {
        this.menuTemplateMapper = menuTemplateMapper;
        this.dishMapper = dishMapper;
    }

    /**
     * 创建模板并绑定菜品。
     *
     * @param name 模板名
     * @param templateType 模板类型
     * @param dishIds 菜品ID列表
     * @return 模板ID
     */
    public Long create(String name, Integer templateType, List<Long> dishIds) {
        for (Long dishId : dishIds) {
            DishDetail dish = dishMapper.selectById(dishId);
            if (dish == null) {
                throw new BizException(40402, "菜品不存在，id=" + dishId);
            }
        }

        MenuTemplate template = new MenuTemplate();
        template.setName(name);
        template.setTemplateType(templateType);
        template.setStatus(1);
        menuTemplateMapper.insert(template);

        List<MenuTemplateDish> items = new ArrayList<>();
        for (int i = 0; i < dishIds.size(); i++) {
            MenuTemplateDish item = new MenuTemplateDish();
            item.setTemplateId(template.getId());
            item.setDishId(dishIds.get(i));
            item.setSortOrder(i);
            items.add(item);
        }
        menuTemplateMapper.batchInsertTemplateDishes(items);
        return template.getId();
    }

    /**
     * 模板详情查询。
     *
     * @param id 模板ID
     * @return 模板
     */
    public MenuTemplate getById(Long id) {
        MenuTemplate template = menuTemplateMapper.selectById(id);
        if (template == null) {
            throw new BizException(40403, "菜单模板不存在，id=" + id);
        }
        return template;
    }

    /**
     * 模板关联菜品 ID 列表。
     *
     * @param templateId 模板ID
     * @return 菜品ID列表
     */
    public List<Long> getDishIds(Long templateId) {
        return menuTemplateMapper.selectDishIdsByTemplateId(templateId);
    }

    /**
     * 模板食材汇总。
     *
     * @param templateId 模板ID
     * @return 汇总列表
     */
    public List<MenuTemplateIngredientSummaryVO> getIngredientSummary(Long templateId) {
        getById(templateId);
        List<Map<String, Object>> rows = menuTemplateMapper.selectIngredientSummaryByTemplateId(templateId);
        List<MenuTemplateIngredientSummaryVO> result = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            MenuTemplateIngredientSummaryVO vo = new MenuTemplateIngredientSummaryVO();
            vo.setIngredientId(((Number) row.get("ingredientId")).longValue());
            vo.setIngredientName((String) row.get("ingredientName"));
            vo.setUnit((String) row.get("unit"));
            Object amount = row.get("totalAmountG");
            if (amount instanceof BigDecimal decimal) {
                vo.setTotalAmountG(decimal);
            } else if (amount instanceof Number number) {
                vo.setTotalAmountG(BigDecimal.valueOf(number.doubleValue()));
            }
            result.add(vo);
        }
        return result;
    }

    /**
     * 模板分页列表（含总数，名称模糊可选）。
     *
     * @param keyword 名称关键字（可空）
     * @param pageNum 页码（从1开始）
     * @param pageSize 每页大小（最大100）
     * @return 分页结果
     */
    public MenuTemplatePage listPage(String keyword, int pageNum, int pageSize) {
        int safePageNum = Math.max(1, pageNum);
        int safePageSize = Math.min(100, Math.max(1, pageSize));
        int offset = (safePageNum - 1) * safePageSize;
        String kw = keyword == null ? null : keyword.trim();
        if (kw != null && kw.isEmpty()) {
            kw = null;
        }
        long total = menuTemplateMapper.countPage(kw);
        List<MenuTemplate> records = menuTemplateMapper.selectPage(kw, offset, safePageSize);
        return new MenuTemplatePage(records, total);
    }

    /**
     * 更新模板及关联菜品（全量替换菜品列表）。
     *
     * @param id 模板ID
     * @param req 更新请求
     */
    public void update(Long id, MenuTemplateUpdateReq req) {
        getById(id);
        for (Long dishId : req.getDishIds()) {
            DishDetail dish = dishMapper.selectById(dishId);
            if (dish == null) {
                throw new BizException(40402, "菜品不存在，id=" + dishId);
            }
        }
        MenuTemplate template = new MenuTemplate();
        template.setId(id);
        template.setName(req.getName());
        template.setTemplateType(req.getTemplateType());
        template.setScene(req.getScene());
        template.setFlavor(req.getFlavor());
        template.setCrowd(req.getCrowd());
        template.setDescription(req.getDescription());
        template.setStatus(req.getStatus());
        template.setRemark(req.getRemark());
        menuTemplateMapper.updateById(template);
        menuTemplateMapper.deleteTemplateDishesByTemplateId(id);
        List<MenuTemplateDish> items = new ArrayList<>();
        for (int i = 0; i < req.getDishIds().size(); i++) {
            MenuTemplateDish item = new MenuTemplateDish();
            item.setTemplateId(id);
            item.setDishId(req.getDishIds().get(i));
            item.setSortOrder(i);
            items.add(item);
        }
        if (!items.isEmpty()) {
            menuTemplateMapper.batchInsertTemplateDishes(items);
        }
    }
}

