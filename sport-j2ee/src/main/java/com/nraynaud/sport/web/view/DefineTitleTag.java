package com.nraynaud.sport.web.view;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class DefineTitleTag extends TagSupport {
    private String value;

    public int doEndTag() throws JspException {
        final ServletRequest request = pageContext.getRequest();
        PageDetail.pageDetailFor(request).setTitle(value);
        return EVAL_PAGE;
    }

    public void setValue(final String value) {
        this.value = value;
    }
}
