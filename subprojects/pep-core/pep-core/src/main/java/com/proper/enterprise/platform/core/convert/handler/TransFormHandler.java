package com.proper.enterprise.platform.core.convert.handler;

public interface TransFormHandler<T, F> {

    /**
     * 从...转换...
     * @param t t
     * @param f f
     */
    void transform(T t, F f);
}
