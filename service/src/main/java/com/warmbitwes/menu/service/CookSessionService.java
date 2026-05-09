package com.warmbitwes.menu.service;

import com.warmbitwes.menu.entity.CookingSession;
import com.warmbitwes.menu.entity.MenuTemplate;
import com.warmbitwes.menu.entity.PrepItem;
import com.warmbitwes.menu.entity.SessionReview;
import com.warmbitwes.menu.exception.BizException;
import com.warmbitwes.menu.mapper.AppUserMapper;
import com.warmbitwes.menu.mapper.CookingSessionMapper;
import com.warmbitwes.menu.mapper.MenuTemplateMapper;
import com.warmbitwes.menu.mapper.P2Mapper;
import com.warmbitwes.menu.vo.PrepItemVO;
import com.warmbitwes.menu.vo.SessionReviewVO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * 做饭会话领域服务。
 */
@Service
public class CookSessionService {

    private static final String DEV_USERNAME = "dev_auto_user";

    private final CookingSessionMapper cookingSessionMapper;
    private final MenuTemplateService menuTemplateService;
    private final AppUserMapper appUserMapper;
    private final MenuTemplateMapper menuTemplateMapper;
    private final P2Mapper p2Mapper;

    public CookSessionService(CookingSessionMapper cookingSessionMapper,
                              MenuTemplateService menuTemplateService,
                              AppUserMapper appUserMapper,
                              MenuTemplateMapper menuTemplateMapper,
                              P2Mapper p2Mapper) {
        this.cookingSessionMapper = cookingSessionMapper;
        this.menuTemplateService = menuTemplateService;
        this.appUserMapper = appUserMapper;
        this.menuTemplateMapper = menuTemplateMapper;
        this.p2Mapper = p2Mapper;
    }

    /**
     * 创建做饭会话。
     *
     * @param templateId 模板ID
     * @param startedAt 开始时间
     * @return 会话ID
     */
    public Long create(Long templateId, LocalDateTime startedAt) {
        MenuTemplate template = menuTemplateService.getById(templateId);

        CookingSession session = new CookingSession();
        session.setUserId(resolveOrCreateDevUserId());
        session.setTemplateId(templateId);
        session.setScene(template.getScene());
        session.setFlavor(template.getFlavor());
        session.setCrowd(template.getCrowd());
        session.setStatus(1);
        session.setStartedAt(startedAt);
        cookingSessionMapper.insert(session);
        return session.getId();
    }

    /**
     * 生成备菜清单。
     *
     * @param sessionId 会话ID
     * @return 备菜项
     */
    public List<PrepItemVO> generatePrepList(Long sessionId) {
        CookingSession session = requireSession(sessionId);
        Long templateId = session.getTemplateId();
        if (templateId == null) {
            return List.of();
        }
        List<Map<String, Object>> summaryRows = menuTemplateMapper.selectIngredientSummaryByTemplateId(templateId);

        p2Mapper.deletePrepItemsBySessionId(sessionId);
        List<PrepItem> items = new ArrayList<>();
        for (Map<String, Object> row : summaryRows) {
            PrepItem item = new PrepItem();
            item.setSessionId(sessionId);
            item.setIngredientId(((Number) row.get("ingredientId")).longValue());
            item.setPlanAmountG(toBigDecimal(row.get("totalAmountG")));
            item.setStatus(1);
            item.setIsShortage(0);
            items.add(item);
        }
        if (!items.isEmpty()) {
            p2Mapper.batchInsertPrepItems(items);
        }
        return listPrepItems(sessionId);
    }

    /**
     * 查询会话备菜清单。
     *
     * @param sessionId 会话ID
     * @return 备菜项列表
     */
    public List<PrepItemVO> listPrepItems(Long sessionId) {
        requireSession(sessionId);
        List<Map<String, Object>> rows = p2Mapper.selectPrepItemsBySessionId(sessionId);
        List<PrepItemVO> result = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            PrepItemVO vo = new PrepItemVO();
            vo.setIngredientId(((Number) row.get("ingredientId")).longValue());
            vo.setIngredientName((String) row.get("ingredientName"));
            vo.setUnit((String) row.get("unit"));
            vo.setPlanAmountG(toBigDecimal(row.get("planAmountG")));
            vo.setStatus(((Number) row.get("status")).intValue());
            vo.setIsShortage(((Number) row.get("isShortage")).intValue());
            result.add(vo);
        }
        return result;
    }

    /**
     * 提交口味点评。
     *
     * @param sessionId 会话ID
     * @param score 口味评分
     */
    public void submitTasteReview(Long sessionId, Integer score) {
        requireSession(sessionId);
        SessionReview review = new SessionReview();
        review.setSessionId(sessionId);
        review.setTasteScore(score);
        p2Mapper.insertSessionReview(review);
    }

    /**
     * 查询会话点评。
     *
     * @param sessionId 会话ID
     * @return 点评列表
     */
    public List<SessionReviewVO> listReviews(Long sessionId) {
        requireSession(sessionId);
        List<Map<String, Object>> rows = p2Mapper.selectReviewsBySessionId(sessionId);
        List<SessionReviewVO> result = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            SessionReviewVO vo = new SessionReviewVO();
            vo.setReviewId(((Number) row.get("reviewId")).longValue());
            vo.setSessionId(((Number) row.get("sessionId")).longValue());
            Object dishId = row.get("dishId");
            if (dishId instanceof Number number) {
                vo.setDishId(number.longValue());
            }
            Object tasteScore = row.get("tasteScore");
            if (tasteScore instanceof Number number) {
                vo.setTasteScore(number.intValue());
            }
            Object difficultyScore = row.get("difficultyScore");
            if (difficultyScore instanceof Number number) {
                vo.setDifficultyScore(number.intValue());
            }
            vo.setRetryIntent((String) row.get("retryIntent"));
            vo.setReviewNote((String) row.get("reviewNote"));
            vo.setCreatedAt((LocalDateTime) row.get("createdAt"));
            result.add(vo);
        }
        return result;
    }

    private Long resolveOrCreateDevUserId() {
        Long userId = appUserMapper.selectAnyUserId();
        if (userId != null) {
            return userId;
        }
        Long existing = appUserMapper.selectIdByUsername(DEV_USERNAME);
        if (existing != null) {
            return existing;
        }
        appUserMapper.insertDevUser(DEV_USERNAME);
        return appUserMapper.selectIdByUsername(DEV_USERNAME);
    }

    private CookingSession requireSession(Long sessionId) {
        CookingSession session = cookingSessionMapper.selectById(sessionId);
        if (session == null) {
            throw new BizException(40404, "会话不存在，id=" + sessionId);
        }
        return session;
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value instanceof BigDecimal decimal) {
            return decimal;
        }
        if (value instanceof Number number) {
            return BigDecimal.valueOf(number.doubleValue());
        }
        return BigDecimal.ZERO;
    }
}

