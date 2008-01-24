package com.nraynaud.sport.web.action;

import org.apache.struts2.config.Result;
import org.apache.struts2.interceptor.ServletResponseAware;

import javax.servlet.http.HttpServletResponse;

@Result(value = "/WEB-INF/pages/error.jsp")
public class ErrorAction implements ServletResponseAware {
    public void setServletResponse(final HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
}
