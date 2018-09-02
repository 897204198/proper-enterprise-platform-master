package com.proper.enterprise.platform.notice.server.api.configurator;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

@Validated
public interface NoticeConfigurator {

    /**
     * 新增配置
     *
     * @param appKey 应用唯一标识
     * @param config 配置信息
     * @return config 配置信息
     */
    Map post(@NotEmpty(message = "notice.server.param.appKey.cantBeEmpty") String appKey,
             @NotEmpty(message = "notice.server.config.content.cantBeEmpty") Map config);

    /**
     * 删除配置
     *
     * @param appKey 应用唯一标识
     */
    void delete(@NotEmpty(message = "notice.server.param.appKey.cantBeEmpty") String appKey);

    /**
     * 修改配置
     *
     * @param appKey 应用唯一标识
     * @param config 配置信息
     * @return config 修改后配置信息
     */
    Map put(@NotEmpty(message = "notice.server.param.appKey.cantBeEmpty") String appKey,
            @NotEmpty(message = "notice.server.config.content.cantBeEmpty") Map config);

    /**
     * 获取配置
     *
     * @param appKey 应用唯一标识
     * @return 配置信息
     */
    Map get(@NotEmpty(message = "notice.server.param.appKey.cantBeEmpty") String appKey);
}
