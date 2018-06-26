package com.proper.enterprise.platform.core.convert.handler;

public interface TargetHandler<T, F> extends TransFormHandler<T, F> {

    /**
     * 从...转换...
     * @param t t
     * @param f f
     */
    void target(T t, F f);

    /**
     * 默认从...转换...
     * @param t t
     * @param f f
     */
    default void transform(T t, F f) {
        target(t, f);
    }
}
