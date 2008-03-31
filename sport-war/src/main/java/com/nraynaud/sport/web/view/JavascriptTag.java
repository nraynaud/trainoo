package com.nraynaud.sport.web.view;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class JavascriptTag extends BodyTagSupport {
    private String src;
    private String content;

    public int doAfterBody() throws JspException {
        content = getBodyContent().getString();
        return SKIP_BODY;
    }

    public int doEndTag() throws JspException {
        PageDetail.detailFor(pageContext.getRequest()).addJavascript(content == null ? "" : content, src);
        return EVAL_PAGE;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(final String src) {
        this.src = src;
    }
}
