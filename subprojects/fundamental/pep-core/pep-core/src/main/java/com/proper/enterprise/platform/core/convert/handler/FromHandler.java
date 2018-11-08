package com.proper.enterprise.platform.core.convert.handler;

public interface FromHandler<T, F> extends TransFormHandler<T, F> {

    /**
     * 从...转换...
     * @param t t
     * @param f f
     */
    void from(T t, F f);

    /**
     * 默认转换
     * @param t t
     * @param f f
     */
    default void transform(T t, F f) {
        from(t, f);
    }
}
