package com.proper.enterprise.platform.core.utils;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;

public class CollectionUtil extends CollectionUtils {

    /**
     * 私有化工具类的构造函数，避免对工具类的实例化
     */
    private CollectionUtil() { }

    /**
     * 静态方法调用私有构造函数，以覆盖对构造函数的测试
     */
    static {
        new CollectionUtil();
    }

    /**
     * 将具体类型元素集合抽象为接口类型元素集合
     *
     * @param collection    具体类型元素集合
     * @param <F>           具体类型
     * @param <T>           接口类型
     * @return 接口类型元素集合
     */
    @SuppressWarnings("unchecked")
    public static <F, T> Collection<T> convert(Collection<F> collection) {
        Collection<T> result = new ArrayList<>(collection.size());
        for (F obj : collection) {
            result.add((T) obj);
        }
        return result;
    }

}
