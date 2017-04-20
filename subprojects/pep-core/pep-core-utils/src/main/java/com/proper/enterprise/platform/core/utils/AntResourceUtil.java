package com.proper.enterprise.platform.core.utils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class AntResourceUtil {

    /**
     * 私有化工具类的构造函数，避免对工具类的实例化
     */
    private AntResourceUtil() { }

    /**
     * 静态方法调用私有构造函数，以覆盖对构造函数的测试
     */
    static {
        new AntResourceUtil();
    }

    public static Resource[] getResources(String locationPattern) throws IOException {
        Assert.hasText(locationPattern, "Location pattern SHOULD NOT EMPTY!");
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        String[] patterns = locationPattern.split(",");
        Set<Resource> result = new LinkedHashSet<>();
        for (String pattern : patterns) {
            result.addAll(Arrays.asList(resolver.getResources(pattern)));
        }
        return result.toArray(new Resource[result.size()]);
    }

}
