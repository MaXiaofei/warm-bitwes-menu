package com.warmbitwes.menu.controller;

import com.warmbitwes.menu.common.ApiResponse;
import com.warmbitwes.menu.dto.CookSessionCreateReq;
import com.warmbitwes.menu.dto.CookSessionCreateResp;
import com.warmbitwes.menu.dto.ReviewTasteReq;
import com.warmbitwes.menu.service.CookSessionService;
import com.warmbitwes.menu.vo.PrepItemVO;
import com.warmbitwes.menu.vo.SessionReviewVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    @Operation(summary = "创建做饭会话", description = "基于菜单模板创建一次新的做饭会话。")
    @PostMapping
    public ApiResponse<CookSessionCreateResp> create(@RequestBody @Valid CookSessionCreateReq req) {
        Long sessionId = cookSessionService.create(req.getTemplateId(), req.getStartedAt());
        return ApiResponse.success(new CookSessionCreateResp(sessionId));
    }

    /**
     * 生成备菜清单。
     *
     * @param sessionId 会话ID
     * @return 备菜项列表
     */
    @Operation(summary = "生成备菜清单", description = "按会话关联模板聚合食材并生成备菜清单。")
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
    @PostMapping("/{sessionId}/reviews/taste")
    public ApiResponse<Void> submitTasteReview(
                                               @Parameter(description = "会话ID", required = true)
                                               @PathVariable("sessionId") Long sessionId,
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
    @GetMapping("/{sessionId}/reviews")
    public ApiResponse<List<SessionReviewVO>> listReviews(
            @Parameter(description = "会话ID", required = true) @PathVariable("sessionId") Long sessionId) {
        return ApiResponse.success(cookSessionService.listReviews(sessionId));
    }
}

