package com.warmbitwes.menu.controller;

import com.warmbitwes.menu.common.ApiResponse;
import com.warmbitwes.menu.dto.CookEventCreateReq;
import com.warmbitwes.menu.dto.CookSessionCreateReq;
import com.warmbitwes.menu.dto.CookSessionCreateResp;
import com.warmbitwes.menu.dto.ReviewTasteReq;
import com.warmbitwes.menu.dto.SessionRetrospectiveUpsertReq;
import com.warmbitwes.menu.security.RequirePermission;
import com.warmbitwes.menu.service.CookSessionMinePage;
import com.warmbitwes.menu.service.CookSessionService;
import com.warmbitwes.menu.vo.CookEventVO;
import com.warmbitwes.menu.vo.PrepItemVO;
import com.warmbitwes.menu.vo.SessionRetrospectiveVO;
import com.warmbitwes.menu.vo.SessionReviewVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 做饭会话接口（V1）。
 */
@Tag(name = "做饭会话")
@RestController
@RequestMapping("/api/cook-sessions")
public class CookSessionController {

    private final CookSessionService cookSessionService;

    public CookSessionController(CookSessionService cookSessionService) {
        this.cookSessionService = cookSessionService;
    }

    /**
     * 创建做饭会话。
     *
     * @param req 创建请求
     * @return 会话ID
     */
    @Operation(summary = "创建做饭会话", description = "菜单模板与自选菜品二选一，开始时间必填。")
    @RequirePermission("cook-session:create")
    @PostMapping
    public ApiResponse<CookSessionCreateResp> create(@RequestBody @Valid CookSessionCreateReq req) {
        Long sessionId = cookSessionService.create(req.getTemplateId(), req.getDishIds(), req.getStartedAt());
        return ApiResponse.success(new CookSessionCreateResp(sessionId));
    }

    /**
     * 我的做饭会话分页列表。
     *
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 分页数据
     */
    @Operation(summary = "我的做饭会话", description = "按当前登录用户分页查询会话摘要。")
    @RequirePermission("cook-session:list-mine")
    @GetMapping("/current/mine")
    public ApiResponse<CookSessionMinePage> listMine(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return ApiResponse.success(cookSessionService.listMineForCurrentUser(pageNum, pageSize));
    }

    /**
     * 查询会话事件时间线。
     *
     * @param sessionId 会话ID
     * @return 事件列表
     */
    @Operation(summary = "查询烹饪事件", description = "按时间升序返回事件列表。")
    @RequirePermission("cook-session:event-read")
    @GetMapping("/{sessionId}/events")
    public ApiResponse<List<CookEventVO>> listEvents(
            @Parameter(description = "会话ID", required = true) @PathVariable("sessionId") Long sessionId) {
        return ApiResponse.success(cookSessionService.listEventsForCurrentUser(sessionId));
    }

    /**
     * 追加烹饪事件。
     *
     * @param sessionId 会话ID
     * @param req 事件内容
     * @return success
     */
    @Operation(summary = "追加烹饪事件", description = "向会话追加一条过程事件。")
    @RequirePermission("cook-session:event-write")
    @PostMapping("/{sessionId}/events")
    public ApiResponse<Void> appendEvent(
            @Parameter(description = "会话ID", required = true) @PathVariable("sessionId") Long sessionId,
            @RequestBody @Valid CookEventCreateReq req) {
        cookSessionService.appendEvent(sessionId, req);
        return ApiResponse.success();
    }

    /**
     * 查询会话复盘。
     *
     * @param sessionId 会话ID
     * @return 复盘或 null
     */
    @Operation(summary = "查询会话复盘", description = "若无复盘则 data 为 null。")
    @RequirePermission("cook-session:retrospective-read")
    @GetMapping("/{sessionId}/retrospective")
    public ApiResponse<SessionRetrospectiveVO> getRetrospective(
            @Parameter(description = "会话ID", required = true) @PathVariable("sessionId") Long sessionId) {
        return ApiResponse.success(cookSessionService.getRetrospectiveForCurrentUser(sessionId));
    }

    /**
     * 写入或更新会话复盘。
     *
     * @param sessionId 会话ID
     * @param req 复盘内容
     * @return success
     */
    @Operation(summary = "写入会话复盘", description = "按会话 UPSERT 复盘记录。")
    @RequirePermission("cook-session:retrospective-write")
    @PutMapping("/{sessionId}/retrospective")
    public ApiResponse<Void> upsertRetrospective(
            @Parameter(description = "会话ID", required = true) @PathVariable("sessionId") Long sessionId,
            @RequestBody @Valid SessionRetrospectiveUpsertReq req) {
        cookSessionService.upsertRetrospectiveForCurrentUser(sessionId, req);
        return ApiResponse.success();
    }

    /**
     * 生成备菜清单。
     *
     * @param sessionId 会话ID
     * @return 备菜项列表
     */
    @Operation(summary = "生成备菜清单", description = "按会话关联模板聚合食材并生成备菜清单。")
    @RequirePermission("cook-session:prep-generate")
    @PostMapping("/{sessionId}/prep-list/generate")
    public ApiResponse<List<PrepItemVO>> generatePrepList(
            @Parameter(description = "会话ID", required = true) @PathVariable("sessionId") Long sessionId) {
        return ApiResponse.success(cookSessionService.generatePrepList(sessionId));
    }

    /**
     * 提交口味评分。
     *
     * @param sessionId 会话ID
     * @param req 评分请求
     * @return success
     */
    @Operation(summary = "提交口味评分", description = "为会话提交一次口味评分（1-5分）。")
    @RequirePermission("cook-session:review-taste")
    @PostMapping("/{sessionId}/reviews/taste")
    public ApiResponse<Void> submitTasteReview(
            @Parameter(description = "会话ID", required = true) @PathVariable("sessionId") Long sessionId,
            @RequestBody @Valid ReviewTasteReq req) {
        cookSessionService.submitTasteReview(sessionId, req.getScore());
        return ApiResponse.success();
    }

    /**
     * 查询点评列表。
     *
     * @param sessionId 会话ID
     * @return 点评列表
     */
    @Operation(summary = "查询会话点评列表", description = "查询会话下已提交的点评记录列表。")
    @RequirePermission("cook-session:review-list")
    @GetMapping("/{sessionId}/reviews")
    public ApiResponse<List<SessionReviewVO>> listReviews(
            @Parameter(description = "会话ID", required = true) @PathVariable("sessionId") Long sessionId) {
        return ApiResponse.success(cookSessionService.listReviews(sessionId));
    }
}
