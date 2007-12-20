package com.nraynaud.sport.web.view;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class LayoutParamsTag extends TagSupport {
    private String title;

    private boolean loginHeader = true;

    public int doEndTag() throws JspException {
        final ServletRequest request = pageContext.getRequest();
        final PageDetail pageDetail = PageDetail.detailFor(request);
        pageDetail.setTitle(title);
        pageDetail.setLoginHeader(loginHeader);
        return EVAL_PAGE;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setLoginHeader(final boolean loginHeader) {
        this.loginHeader = loginHeader;
    }
}
