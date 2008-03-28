package com.nraynaud.sport.web.view;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class HeaderTag extends BodyTagSupport {
    public int doAfterBody() throws JspException {
        final BodyContent content = getBodyContent();
        PageDetail.detailFor(pageContext.getRequest()).addHeader(content.getString());
        return SKIP_BODY;
    }
}
