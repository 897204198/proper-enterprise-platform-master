package com.proper.enterprise.platform.core.convert.handler;

public interface FromHandler<T, F> extends TransFormHandler<T, F> {

    void from(T t, F f);

    default void transform(T t, F f) {
        from(t, f);
    }
}
