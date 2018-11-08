package com.proper.enterprise.platform.sys.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "pep.sys.datadic")
public class SysDataDicProperties {

    /**
     * 数据字典类型属性在持久化时, 数据保存分隔符
     */
    private String separator = ";";

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }
}
