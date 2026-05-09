package com.warmbitwes.menu.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.warmbitwes.menu.entity.CookingSession;
import com.warmbitwes.menu.entity.DishDetail;
import com.warmbitwes.menu.entity.MenuTemplate;
import com.warmbitwes.menu.entity.SessionDish;
import com.warmbitwes.menu.exception.BizException;
import com.warmbitwes.menu.mapper.AppUserMapper;
import com.warmbitwes.menu.mapper.CookEventMapper;
import com.warmbitwes.menu.mapper.CookingSessionMapper;
import com.warmbitwes.menu.mapper.DishMapper;
import com.warmbitwes.menu.mapper.MenuTemplateMapper;
import com.warmbitwes.menu.mapper.P2Mapper;
import com.warmbitwes.menu.mapper.SessionRetrospectiveMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CookSessionServiceTest {

    @Mock
    private CookingSessionMapper cookingSessionMapper;
    @Mock
    private MenuTemplateService menuTemplateService;
    @Mock
    private AppUserMapper appUserMapper;
    @Mock
    private MenuTemplateMapper menuTemplateMapper;
    @Mock
    private P2Mapper p2Mapper;
    @Mock
    private DishMapper dishMapper;
    @Mock
    private CookEventMapper cookEventMapper;
    @Mock
    private SessionRetrospectiveMapper sessionRetrospectiveMapper;

    @InjectMocks
    private CookSessionService cookSessionService;

    @Test
    void create_should_throw_when_neither_template_nor_dishes() {
        BizException ex = assertThrows(
                BizException.class,
                () -> cookSessionService.create(null, List.of(), LocalDateTime.now())
        );
        assertEquals(10013, ex.getCode());
    }

    @Test
    void create_should_throw_when_both_template_and_dishes() {
        BizException ex = assertThrows(
                BizException.class,
                () -> cookSessionService.create(1L, List.of(2L), LocalDateTime.now())
        );
        assertEquals(10014, ex.getCode());
    }

    @Test
    void create_should_use_template_path() {
        when(appUserMapper.selectAnyUserId()).thenReturn(1L);
        MenuTemplate t = new MenuTemplate();
        t.setScene("s");
        t.setFlavor("f");
        t.setCrowd("c");
        when(menuTemplateService.getById(9L)).thenReturn(t);
        doAnswer(inv -> {
            CookingSession s = inv.getArgument(0);
            s.setId(50L);
            return 1;
        }).when(cookingSessionMapper).insert(any(CookingSession.class));

        Long id = cookSessionService.create(9L, List.of(), LocalDateTime.parse("2026-05-09T12:00:00"));
        assertEquals(50L, id);
        verify(p2Mapper, never()).batchInsertSessionDishes(anyList());
    }

    @Test
    void create_should_insert_session_dishes_when_dish_only() {
        when(appUserMapper.selectAnyUserId()).thenReturn(1L);
        DishDetail d1 = new DishDetail();
        d1.setId(1L);
        when(dishMapper.selectById(1L)).thenReturn(d1);
        DishDetail d2 = new DishDetail();
        d2.setId(2L);
        when(dishMapper.selectById(2L)).thenReturn(d2);
        doAnswer(inv -> {
            CookingSession s = inv.getArgument(0);
            s.setId(77L);
            return 1;
        }).when(cookingSessionMapper).insert(any(CookingSession.class));

        Long id = cookSessionService.create(null, List.of(1L, 2L), LocalDateTime.parse("2026-05-09T11:00:00"));
        assertEquals(77L, id);
        verify(p2Mapper).batchInsertSessionDishes(anyList());
    }

    @Test
    void create_should_map_session_dish_sort_order() {
        when(appUserMapper.selectAnyUserId()).thenReturn(1L);
        DishDetail d = new DishDetail();
        d.setId(3L);
        when(dishMapper.selectById(3L)).thenReturn(d);
        doAnswer(inv -> {
            CookingSession s = inv.getArgument(0);
            s.setId(88L);
            return 1;
        }).when(cookingSessionMapper).insert(any(CookingSession.class));
        doAnswer(inv -> {
            @SuppressWarnings("unchecked")
            List<SessionDish> rows = inv.getArgument(0);
            assertEquals(1, rows.size());
            assertEquals(88L, rows.get(0).getSessionId());
            assertEquals(3L, rows.get(0).getDishId());
            assertEquals(0, rows.get(0).getSortOrder());
            return 1;
        }).when(p2Mapper).batchInsertSessionDishes(anyList());

        cookSessionService.create(null, List.of(3L), LocalDateTime.now());
    }
}
