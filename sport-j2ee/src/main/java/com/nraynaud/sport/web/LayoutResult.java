package com.nraynaud.sport.web;

import com.opensymphony.xwork2.ActionInvocation;
import com.nraynaud.sport.web.view.PageDetail;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.components.Include;
import org.apache.struts2.dispatcher.ServletDispatcherResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.StringWriter;

public class LayoutResult extends ServletDispatcherResult {
    public void doExecute(final String finalLocation, final ActionInvocation invocation) throws Exception {
        final StringWriter stringWriter = new StringWriter();
        final HttpServletResponse response = ServletActionContext.getResponse();
        final HttpServletRequest request = ServletActionContext.getRequest();
        Include.include(finalLocation, stringWriter, request, response);
        PageDetail.pageDetailFor(request).setContent(stringWriter.toString());
        super.doExecute("/WEB-INF/application.jsp", invocation);
    }
}
