package com.proper.enterprise.platform.core.jpa;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "pep.core.jpa")
public class CoreJPAProperties {

    /**
     * 固定数目连接池数
     * 连接数需根据 CPU 数量进行调整，每个 CPU 调度的进程数不应过多，2~10 倍之间为宜\
     * CPU 密集型倍数小些，IO 密集型倍数高些
     * 例如 24 个 CPU，配置 144 个静态连接池，每个 CPU 负责管理 6 个进程
     */
    private int fixedPoolSize = Runtime.getRuntime().availableProcessors() * 6;

    public int getFixedPoolSize() {
        return fixedPoolSize;
    }

    public void setFixedPoolSize(int fixedPoolSize) {
        this.fixedPoolSize = fixedPoolSize;
    }
}
