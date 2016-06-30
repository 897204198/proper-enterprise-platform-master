package com.proper.enterprise.platform.core.converter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 接口集合转换器
 */
public class InterfaceCollectionConverter {

    /**
     * 私有化工具类的构造函数，避免对工具类的实例化
     */
    private InterfaceCollectionConverter() { }

    /**
     * 静态方法调用私有构造函数，以覆盖对构造函数的测试
     */
    static {
        new InterfaceCollectionConverter();
    }

    /**
     * 将具体类型元素集合抽象为接口类型元素集合
     *
     * @param collection    具体类型元素集合
     * @param <F>           具体类型
     * @param <T>           接口类型
     * @return 接口类型元素集合
     */
    public static <F, T> Collection<T> convert(Collection<F> collection) {
        Collection<T> result = new ArrayList<>(collection.size());
        for (F obj : collection) {
            result.add((T) obj);
        }
        return result;
    }

}
