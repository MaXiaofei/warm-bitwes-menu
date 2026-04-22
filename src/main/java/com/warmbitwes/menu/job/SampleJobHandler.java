package com.warmbitwes.menu.job;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.stereotype.Component;

/**
 * XXL-JOB 示例处理器：
 * 需在 XXL-JOB admin 后台配置对应 JobHandler 的调用。
 */
@Component
@JobHandler(value = "sampleJobHandler")
public class SampleJobHandler extends IJobHandler {

    @Override
    public ReturnT<String> execute(String param) {
        String p = (param == null) ? "" : param.trim();
        return ReturnT.SUCCESS("sampleJobHandler executed, param=" + p);
    }
}

