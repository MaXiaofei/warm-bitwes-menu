package com.warmbitwes.menu.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会话复盘写入请求（UPSERT）。
 */
@Data
@NoArgsConstructor
public class SessionRetrospectiveUpsertReq {

    @Size(max = 1000, message = "summary长度不能超过1000")
    private String summary;

    @Size(max = 1000, message = "improvement长度不能超过1000")
    private String improvement;

    @Size(max = 1000, message = "retryAdvice长度不能超过1000")
    private String retryAdvice;

    @Size(max = 512, message = "remark长度不能超过512")
    private String remark;
}
