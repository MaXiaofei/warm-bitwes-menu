package com.warmbitwes.menu.controller;

import com.warmbitwes.menu.common.ApiResponse;
import com.warmbitwes.menu.dto.DropdownOptionCreateReq;
import com.warmbitwes.menu.dto.DropdownOptionUpdateReq;
import com.warmbitwes.menu.dto.IdResp;
import com.warmbitwes.menu.security.RequirePermission;
import com.warmbitwes.menu.service.DropdownOptionService;
import com.warmbitwes.menu.vo.DropdownOptionAdminVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端：下拉选项配置 CRUD。
 */
@Tag(name = "管理端-下拉选项配置")
@RestController
@RequestMapping("/api/admin/dropdown-options")
public class AdminDropdownOptionController {

    private final DropdownOptionService dropdownOptionService;

    public AdminDropdownOptionController(DropdownOptionService dropdownOptionService) {
        this.dropdownOptionService = dropdownOptionService;
    }

    /**
     * 条件列表。
     *
     * @param category 分类编码，可选；不传则返回全部
     * @return 配置列表
     */
    @Operation(summary = "下拉选项列表（管理端）", description = "可按分类筛选，含启用/停用与排序。")
    @GetMapping
    @RequirePermission("dropdown-option:list")
    public ApiResponse<List<DropdownOptionAdminVO>> list(
            @Parameter(description = "分类编码，空缺表示全部") @RequestParam(value = "category", required = false)
            String category) {
        return ApiResponse.success(dropdownOptionService.listForAdmin(category));
    }

    /**
     * 新增一条配置。
     *
     * @param req 请求体
     * @return 新建 id
     */
    @Operation(summary = "新增下拉选项", description = "同一 category 下 optionCode 全局唯一（由表约束保障）。")
    @PostMapping
    @RequirePermission("dropdown-option:create")
    public ApiResponse<IdResp> create(@RequestBody @Valid DropdownOptionCreateReq req) {
        Long id = dropdownOptionService.create(req);
        return ApiResponse.success(new IdResp(id));
    }

    /**
     * 修改配置。
     *
     * @param id 主键
     * @param req 请求体
     * @return success
     */
    @Operation(summary = "更新下拉选项", description = "按 id 覆盖分类、编码、文案、排序与启用状态。")
    @PutMapping("/{id}")
    @RequirePermission("dropdown-option:update")
    public ApiResponse<Void> update(@Parameter(description = "主键") @PathVariable("id") Long id,
                                   @RequestBody @Valid DropdownOptionUpdateReq req) {
        dropdownOptionService.update(id, req);
        return ApiResponse.success();
    }

    /**
     * 删除配置。
     *
     * @param id 主键
     * @return success
     */
    @Operation(summary = "删除下拉选项", description = "物理删除；请谨慎操作。")
    @DeleteMapping("/{id}")
    @RequirePermission("dropdown-option:delete")
    public ApiResponse<Void> delete(@Parameter(description = "主键") @PathVariable("id") Long id) {
        dropdownOptionService.delete(id);
        return ApiResponse.success();
    }
}
