package com.nraynaud.sport.web.view;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class ContentTag extends TagSupport {
    public int doStartTag() throws JspException {
        try {
            final PageDetail detail = (PageDetail) pageContext.getRequest().getAttribute("detail");
            pageContext.getOut().append(detail.getContent());
            return SKIP_BODY;
        } catch (IOException e) {
            throw new JspException(e);
        }
    }
}
