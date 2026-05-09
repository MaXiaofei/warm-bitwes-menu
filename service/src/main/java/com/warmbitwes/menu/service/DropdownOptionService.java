package com.warmbitwes.menu.service;

import com.warmbitwes.menu.dto.DropdownOptionCreateReq;
import com.warmbitwes.menu.dto.DropdownOptionUpdateReq;
import com.warmbitwes.menu.entity.BizDropdownOption;
import com.warmbitwes.menu.exception.BizException;
import com.warmbitwes.menu.mapper.BizDropdownOptionMapper;
import com.warmbitwes.menu.vo.DropdownOptionAdminVO;
import com.warmbitwes.menu.vo.DropdownOptionItemVO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * 下拉选项配置服务。
 */
@Service
public class DropdownOptionService {

    private final BizDropdownOptionMapper bizDropdownOptionMapper;

    public DropdownOptionService(BizDropdownOptionMapper bizDropdownOptionMapper) {
        this.bizDropdownOptionMapper = bizDropdownOptionMapper;
    }

    /**
     * 按分类返回已启用项（下拉用）。
     *
     * @param category 分类编码（如 MENU_TEMPLATE_TYPE）
     * @return optionCode → optionLabel
     */
    public List<DropdownOptionItemVO> listEnabledItems(String category) {
        if (category == null || category.isBlank()) {
            throw new BizException(10006, "category不能为空");
        }
        String cat = category.trim();
        return bizDropdownOptionMapper.selectEnabledByCategory(cat).stream()
                .map(this::toItemVo)
                .collect(Collectors.toList());
    }

    /**
     * 已启用项涉及的全部分类编码。
     *
     * @return 分类列表
     */
    public List<String> listCategories() {
        return bizDropdownOptionMapper.selectDistinctCategories();
    }

    /**
     * 管理端列表。
     *
     * @param category 分类筛选，可空表示全部
     * @return 完整记录
     */
    public List<DropdownOptionAdminVO> listForAdmin(String category) {
        String cat = category == null || category.isBlank() ? null : category.trim();
        return bizDropdownOptionMapper.selectForAdmin(cat).stream()
                .map(this::toAdminVo)
                .collect(Collectors.toList());
    }

    /**
     * 新增选项。
     *
     * @param req 请求
     * @return 新记录 id
     */
    public Long create(DropdownOptionCreateReq req) {
        BizDropdownOption row = new BizDropdownOption();
        row.setCategory(req.getCategory().trim());
        row.setOptionCode(req.getOptionCode().trim());
        row.setOptionLabel(req.getOptionLabel().trim());
        row.setSortOrder(req.getSortOrder());
        row.setEnabled(req.getEnabled());
        row.setRemark(req.getRemark());
        bizDropdownOptionMapper.insert(row);
        return row.getId();
    }

    /**
     * 更新选项。
     *
     * @param id 主键
     * @param req 请求
     */
    public void update(Long id, DropdownOptionUpdateReq req) {
        BizDropdownOption existing = bizDropdownOptionMapper.selectById(id);
        if (existing == null) {
            throw new BizException(40405, "下拉选项不存在，id=" + id);
        }
        existing.setCategory(req.getCategory().trim());
        existing.setOptionCode(req.getOptionCode().trim());
        existing.setOptionLabel(req.getOptionLabel().trim());
        existing.setSortOrder(req.getSortOrder());
        existing.setEnabled(req.getEnabled());
        existing.setRemark(req.getRemark());
        bizDropdownOptionMapper.updateById(existing);
    }

    /**
     * 删除选项。
     *
     * @param id 主键
     */
    public void delete(Long id) {
        BizDropdownOption existing = bizDropdownOptionMapper.selectById(id);
        if (existing == null) {
            throw new BizException(40405, "下拉选项不存在，id=" + id);
        }
        bizDropdownOptionMapper.deleteById(id);
    }

    private DropdownOptionItemVO toItemVo(BizDropdownOption row) {
        DropdownOptionItemVO vo = new DropdownOptionItemVO();
        vo.setCode(row.getOptionCode());
        vo.setLabel(row.getOptionLabel());
        vo.setSortOrder(row.getSortOrder());
        return vo;
    }

    private DropdownOptionAdminVO toAdminVo(BizDropdownOption row) {
        DropdownOptionAdminVO vo = new DropdownOptionAdminVO();
        vo.setId(row.getId());
        vo.setCategory(row.getCategory());
        vo.setOptionCode(row.getOptionCode());
        vo.setOptionLabel(row.getOptionLabel());
        vo.setSortOrder(row.getSortOrder());
        vo.setEnabled(row.getEnabled());
        vo.setRemark(row.getRemark());
        vo.setCreatedAt(row.getCreatedAt());
        vo.setUpdatedAt(row.getUpdatedAt());
        return vo;
    }
}
