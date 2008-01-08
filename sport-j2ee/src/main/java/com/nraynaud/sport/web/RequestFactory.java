package com.nraynaud.sport.web;

import com.nraynaud.sport.UserStore;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.FactoryBean;

import javax.servlet.http.HttpServletRequest;

public class RequestFactory implements FactoryBean {

    public Object getObject() throws Exception {
        final HttpServletRequest servletRequest = ServletActionContext.getRequest();
        if (servletRequest == null)
            throw new NullPointerException("servletRequest is null!!");
        return servletRequest;
    }

    @SuppressWarnings({"RawUseOfParameterizedType"})
    public Class getObjectType() {
        return HttpServletRequest.class;
    }

    public boolean isSingleton() {
        return false;
    }
}
