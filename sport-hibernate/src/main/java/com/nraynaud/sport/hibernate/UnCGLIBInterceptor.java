package com.nraynaud.sport.hibernate;

import org.hibernate.EmptyInterceptor;
import org.springframework.util.ClassUtils;

//to correct interaction bug between Spring and Hibernate (with such names, everybody would have gessed problems)
public class UnCGLIBInterceptor extends EmptyInterceptor {
    public String getEntityName(final Object object) {
        return (object == null ? null : ClassUtils.getShortName(object.getClass()));
    }
}
