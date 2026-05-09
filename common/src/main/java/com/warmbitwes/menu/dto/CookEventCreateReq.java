package com.warmbitwes.menu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 烹饪事件创建请求。
 */
@Data
@NoArgsConstructor
public class CookEventCreateReq {

    @NotBlank(message = "eventType不能为空")
    @Size(max = 32, message = "eventType长度不能超过32")
    private String eventType;

    @NotNull(message = "eventTime不能为空")
    private LocalDateTime eventTime;

    @Size(max = 1000, message = "content长度不能超过1000")
    private String content;

    @Size(max = 512, message = "remark长度不能超过512")
    private String remark;
}
