package com.warmbitwes.menu.controller;

import com.warmbitwes.menu.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查接口（白名单）。
 */
@RestController
@RequestMapping("/api")
public class HealthController {

    /**
     * 健康检查。
     *
     * @return ok
     */
    @GetMapping("/health")
    public ApiResponse<String> health() {
        return ApiResponse.success("ok");
    }
}
