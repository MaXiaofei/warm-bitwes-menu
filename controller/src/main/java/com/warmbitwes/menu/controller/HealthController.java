package com.warmbitwes.menu.controller;

import com.warmbitwes.menu.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查接口（白名单）。
 */
@Tag(name = "系统健康")
@RestController
@RequestMapping("/api")
public class HealthController {

    /**
     * 健康检查。
     *
     * @return ok
     */
    @Operation(summary = "健康检查", description = "用于服务存活探测，返回固定值ok。")
    @GetMapping("/health")
    public ApiResponse<String> health() {
        return ApiResponse.success("ok");
    }
}
