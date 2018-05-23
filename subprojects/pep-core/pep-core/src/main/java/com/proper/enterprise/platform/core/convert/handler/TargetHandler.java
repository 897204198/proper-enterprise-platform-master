package com.proper.enterprise.platform.core.convert.handler;

public interface TargetHandler<T, F> extends TransFormHandler<T, F> {
    void target(T t, F f);

    default void transform(T t, F f) {
        target(t, f);
    }
}
