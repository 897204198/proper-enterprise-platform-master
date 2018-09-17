package com.proper.enterprise.platform.notice.server.api.configurator;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Validated
public interface NoticeConfigurator {

    /**
     * 新增配置
     *
     * @param appKey  应用唯一标识
     * @param config  配置信息
     * @param request request
     * @return config 配置信息
     */
    Map post(@NotEmpty(message = "{notice.server.param.appKey.cantBeEmpty}") String appKey,
             @NotEmpty(message = "{notice.server.config.content.cantBeEmpty}") Map<String, Object> config,
             HttpServletRequest request);

    /**
     * 删除配置
     *
     * @param appKey  应用唯一标识
     * @param request request
     */
    void delete(@NotEmpty(message = "{notice.server.param.appKey.cantBeEmpty}") String appKey,
                HttpServletRequest request);

    /**
     * 修改配置
     *
     * @param appKey  应用唯一标识
     * @param config  配置信息
     * @param request request
     * @return config 修改后配置信息
     */
    Map put(@NotEmpty(message = "{notice.server.param.appKey.cantBeEmpty}") String appKey,
            @NotEmpty(message = "{notice.server.config.content.cantBeEmpty}") Map<String, Object> config,
            HttpServletRequest request);

    /**
     * 获取配置
     *
     * @param appKey  应用唯一标识
     * @param request request
     * @return 配置信息
     */
    Map get(@NotEmpty(message = "{notice.server.param.appKey.cantBeEmpty}") String appKey,
            HttpServletRequest request);
}
