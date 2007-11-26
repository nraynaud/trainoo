package com.nraynaud.sport.web;

import java.lang.ref.SoftReference;

public abstract class SoftThreadLocal<T> {
    final ThreadLocal<SoftReference<T>> ref = new ThreadLocal<SoftReference<T>>() {
        protected synchronized SoftReference<T> initialValue() {
            return new SoftReference<T>(createValue());
        }
    };

    public T get() {
        T value = ref.get().get();
        if (value == null) {
            value = createValue();
            ref.set(new SoftReference<T>(value));
        }
        return value;
    }

    protected abstract T createValue();
}
