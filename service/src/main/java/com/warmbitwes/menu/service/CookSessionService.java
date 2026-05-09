package com.warmbitwes.menu.service;

import com.warmbitwes.menu.dto.CookEventCreateReq;
import com.warmbitwes.menu.dto.SessionRetrospectiveUpsertReq;
import com.warmbitwes.menu.entity.CookEvent;
import com.warmbitwes.menu.entity.CookingSession;
import com.warmbitwes.menu.entity.CookingSessionMineRow;
import com.warmbitwes.menu.entity.DishDetail;
import com.warmbitwes.menu.entity.MenuTemplate;
import com.warmbitwes.menu.entity.PrepItem;
import com.warmbitwes.menu.entity.SessionDish;
import com.warmbitwes.menu.entity.SessionRetrospective;
import com.warmbitwes.menu.entity.SessionReview;
import com.warmbitwes.menu.exception.BizException;
import com.warmbitwes.menu.mapper.AppUserMapper;
import com.warmbitwes.menu.mapper.CookEventMapper;
import com.warmbitwes.menu.mapper.CookingSessionMapper;
import com.warmbitwes.menu.mapper.DishMapper;
import com.warmbitwes.menu.mapper.MenuTemplateMapper;
import com.warmbitwes.menu.mapper.P2Mapper;
import com.warmbitwes.menu.mapper.SessionRetrospectiveMapper;
import com.warmbitwes.menu.security.LoginUser;
import com.warmbitwes.menu.security.SecurityContextHolder;
import com.warmbitwes.menu.vo.CookEventVO;
import com.warmbitwes.menu.vo.CookSessionMineItemVO;
import com.warmbitwes.menu.vo.PrepItemVO;
import com.warmbitwes.menu.vo.SessionRetrospectiveVO;
import com.warmbitwes.menu.vo.SessionReviewVO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
    private final DishMapper dishMapper;
    private final CookEventMapper cookEventMapper;
    private final SessionRetrospectiveMapper sessionRetrospectiveMapper;

    public CookSessionService(CookingSessionMapper cookingSessionMapper,
                              MenuTemplateService menuTemplateService,
                              AppUserMapper appUserMapper,
                              MenuTemplateMapper menuTemplateMapper,
                              P2Mapper p2Mapper,
                              DishMapper dishMapper,
                              CookEventMapper cookEventMapper,
                              SessionRetrospectiveMapper sessionRetrospectiveMapper) {
        this.cookingSessionMapper = cookingSessionMapper;
        this.menuTemplateService = menuTemplateService;
        this.appUserMapper = appUserMapper;
        this.menuTemplateMapper = menuTemplateMapper;
        this.p2Mapper = p2Mapper;
        this.dishMapper = dishMapper;
        this.cookEventMapper = cookEventMapper;
        this.sessionRetrospectiveMapper = sessionRetrospectiveMapper;
    }

    /**
     * 创建做饭会话：菜单模板与自选菜品二选一，开始时间必填。
     *
     * @param templateId 模板ID（可选）
     * @param dishIds    自选菜品ID列表（可选，与模板二选一）
     * @param startedAt  开始时间
     * @return 会话ID
     */
    public Long create(Long templateId, List<Long> dishIds, LocalDateTime startedAt) {
        if (startedAt == null) {
            throw new BizException(10012, "开始时间不能为空");
        }
        List<Long> dishList = dishIds == null ? List.of() : dishIds.stream().filter(id -> id != null).toList();
        boolean hasTemplate = templateId != null;
        boolean hasDishes = !dishList.isEmpty();
        if (!hasTemplate && !hasDishes) {
            throw new BizException(10013, "请选择菜单模板或自选菜品");
        }
        if (hasTemplate && hasDishes) {
            throw new BizException(10014, "菜单模板与自选菜品仅能选一种");
        }
        if (hasTemplate) {
            return createFromTemplate(templateId, startedAt);
        }
        return createFromDishList(dishList, startedAt);
    }

    private Long createFromTemplate(Long templateId, LocalDateTime startedAt) {
        MenuTemplate template = menuTemplateService.getById(templateId);

        CookingSession session = new CookingSession();
        session.setUserId(resolveSessionUserId());
        session.setTemplateId(templateId);
        session.setScene(template.getScene());
        session.setFlavor(template.getFlavor());
        session.setCrowd(template.getCrowd());
        session.setStatus(1);
        session.setStartedAt(startedAt);
        cookingSessionMapper.insert(session);
        return session.getId();
    }

    private Long createFromDishList(List<Long> dishIds, LocalDateTime startedAt) {
        List<Long> orderedUnique = new ArrayList<>(new LinkedHashSet<>(dishIds));
        for (Long dishId : orderedUnique) {
            DishDetail dish = dishMapper.selectById(dishId);
            if (dish == null) {
                throw new BizException(40402, "菜品不存在，id=" + dishId);
            }
        }

        CookingSession session = new CookingSession();
        session.setUserId(resolveSessionUserId());
        session.setTemplateId(null);
        session.setScene(null);
        session.setFlavor(null);
        session.setCrowd(null);
        session.setStatus(1);
        session.setStartedAt(startedAt);
        cookingSessionMapper.insert(session);
        Long sessionId = session.getId();

        List<SessionDish> rows = new ArrayList<>();
        int sort = 0;
        for (Long dishId : orderedUnique) {
            SessionDish row = new SessionDish();
            row.setSessionId(sessionId);
            row.setDishId(dishId);
            row.setSortOrder(sort++);
            rows.add(row);
        }
        if (!rows.isEmpty()) {
            p2Mapper.batchInsertSessionDishes(rows);
        }
        return sessionId;
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

    /**
     * 分页查询当前登录用户的做饭会话。
     *
     * @param pageNum 页码（从1开始）
     * @param pageSize 每页条数（最大100）
     * @return 分页数据
     */
    public CookSessionMinePage listMineForCurrentUser(int pageNum, int pageSize) {
        Long userId = requireUserId();
        int safePageNum = Math.max(1, pageNum);
        int safePageSize = Math.min(100, Math.max(1, pageSize));
        int offset = (safePageNum - 1) * safePageSize;
        long total = cookingSessionMapper.countByUserId(userId);
        List<CookSessionMineItemVO> records = cookingSessionMapper.selectMineByUserId(userId, offset, safePageSize).stream()
                .map(row -> {
                    CookSessionMineItemVO vo = new CookSessionMineItemVO();
                    vo.setId(row.getId());
                    vo.setTemplateId(row.getTemplateId());
                    vo.setTemplateName(row.getTemplateName());
                    vo.setStartedAt(row.getStartedAt());
                    vo.setStatus(row.getStatus());
                    return vo;
                })
                .collect(Collectors.toList());
        return new CookSessionMinePage(records, total);
    }

    /**
     * 追加烹饪事件（仅会话所属用户）。
     *
     * @param sessionId 会话ID
     * @param req 事件内容
     */
    public void appendEvent(Long sessionId, CookEventCreateReq req) {
        assertSessionOwnedByCurrentUser(sessionId);
        CookEvent row = new CookEvent();
        row.setSessionId(sessionId);
        row.setEventType(req.getEventType());
        row.setEventTime(req.getEventTime());
        row.setContent(req.getContent());
        row.setRemark(req.getRemark());
        cookEventMapper.insert(row);
    }

    /**
     * 查询会话事件时间线（仅会话所属用户）。
     *
     * @param sessionId 会话ID
     * @return 事件列表
     */
    public List<CookEventVO> listEventsForCurrentUser(Long sessionId) {
        assertSessionOwnedByCurrentUser(sessionId);
        return cookEventMapper.selectBySessionIdOrderByEventTimeAsc(sessionId).stream()
                .map(this::toCookEventVo)
                .collect(Collectors.toList());
    }

    /**
     * 查询复盘（仅会话所属用户）。
     *
     * @param sessionId 会话ID
     * @return 复盘或 null
     */
    public SessionRetrospectiveVO getRetrospectiveForCurrentUser(Long sessionId) {
        assertSessionOwnedByCurrentUser(sessionId);
        SessionRetrospective row = sessionRetrospectiveMapper.selectBySessionId(sessionId);
        if (row == null) {
            return null;
        }
        SessionRetrospectiveVO vo = new SessionRetrospectiveVO();
        vo.setId(row.getId());
        vo.setSessionId(row.getSessionId());
        vo.setSummary(row.getSummary());
        vo.setImprovement(row.getImprovement());
        vo.setRetryAdvice(row.getRetryAdvice());
        vo.setRemark(row.getRemark());
        vo.setUpdatedAt(row.getUpdatedAt());
        return vo;
    }

    private CookEventVO toCookEventVo(CookEvent e) {
        CookEventVO vo = new CookEventVO();
        vo.setId(e.getId());
        vo.setSessionId(e.getSessionId());
        vo.setEventType(e.getEventType());
        vo.setEventTime(e.getEventTime());
        vo.setContent(e.getContent());
        vo.setRemark(e.getRemark());
        vo.setCreatedAt(e.getCreatedAt());
        return vo;
    }

    /**
     * 写入或更新复盘（仅会话所属用户）。
     *
     * @param sessionId 会话ID
     * @param req 复盘内容
     */
    public void upsertRetrospectiveForCurrentUser(Long sessionId, SessionRetrospectiveUpsertReq req) {
        assertSessionOwnedByCurrentUser(sessionId);
        SessionRetrospective row = new SessionRetrospective();
        row.setSessionId(sessionId);
        row.setSummary(req.getSummary());
        row.setImprovement(req.getImprovement());
        row.setRetryAdvice(req.getRetryAdvice());
        row.setRemark(req.getRemark());
        sessionRetrospectiveMapper.upsert(row);
    }

    private Long requireUserId() {
        LoginUser u = SecurityContextHolder.get();
        if (u == null) {
            throw new BizException(10002, "未登录或令牌无效");
        }
        return u.getUserId();
    }

    private void assertSessionOwnedByCurrentUser(Long sessionId) {
        Long userId = requireUserId();
        CookingSession session = requireSession(sessionId);
        if (session.getUserId() == null || !session.getUserId().equals(userId)) {
            throw new BizException(10015, "无权访问该会话");
        }
    }

    private Long resolveSessionUserId() {
        LoginUser u = SecurityContextHolder.get();
        if (u != null) {
            return u.getUserId();
        }
        return resolveOrCreateDevUserId();
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

