package com.nraynaud.sport.hibernate;

import org.hibernate.EmptyInterceptor;
import org.springframework.util.ClassUtils;

public class UnCGLIBInterceptor extends EmptyInterceptor {
    public String getEntityName(final Object object) {
        return (object == null ? null : ClassUtils.getShortName(object.getClass()));
    }
}
