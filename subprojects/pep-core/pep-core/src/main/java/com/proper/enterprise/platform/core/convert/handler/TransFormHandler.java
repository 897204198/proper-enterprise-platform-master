package com.proper.enterprise.platform.core.convert.handler;

public interface TransFormHandler<T, F> {

    void transform(T t, F f);
}
