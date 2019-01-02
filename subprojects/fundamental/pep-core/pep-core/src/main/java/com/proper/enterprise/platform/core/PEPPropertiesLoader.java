package com.proper.enterprise.platform.core;

/**
 * 配置文件读取类
 * <p>
 * 从spring上下文中优先读取，若spring上下文中没有则实例化配置并返回
 */
public class PEPPropertiesLoader {

    private PEPPropertiesLoader() {

    }

    public static <T> T load(Class<T> properties) {
        try {
            if (null == PEPApplicationContext.getApplicationContext()) {
                return properties.newInstance();
            }
            return PEPApplicationContext.getBean(properties);
        } catch (Exception e) {
            try {
                return properties.newInstance();
            } catch (Exception ex) {
                throw new RuntimeException("read properties error", ex);
            }
        }
    }
}
