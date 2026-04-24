package com.warmbitwes.menu.controller;

import com.warmbitwes.menu.common.ApiResponse;
import com.warmbitwes.menu.dto.CookSessionCreateReq;
import com.warmbitwes.menu.dto.CookSessionCreateResp;
import com.warmbitwes.menu.dto.ReviewTasteReq;
import com.warmbitwes.menu.service.CookSessionService;
import com.warmbitwes.menu.vo.PrepItemVO;
import com.warmbitwes.menu.vo.SessionReviewVO;
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
    @PostMapping("/{sessionId}/prep-list/generate")
    public ApiResponse<List<PrepItemVO>> generatePrepList(@PathVariable("sessionId") Long sessionId) {
        return ApiResponse.success(cookSessionService.generatePrepList(sessionId));
    }

    /**
     * 提交口味评分。
     *
     * @param sessionId 会话ID
     * @param req 评分请求
     * @return success
     */
    @PostMapping("/{sessionId}/reviews/taste")
    public ApiResponse<Void> submitTasteReview(@PathVariable("sessionId") Long sessionId,
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
    @GetMapping("/{sessionId}/reviews")
    public ApiResponse<List<SessionReviewVO>> listReviews(@PathVariable("sessionId") Long sessionId) {
        return ApiResponse.success(cookSessionService.listReviews(sessionId));
    }
}

