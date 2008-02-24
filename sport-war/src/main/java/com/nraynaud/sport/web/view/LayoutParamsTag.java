package com.nraynaud.sport.web.view;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class LayoutParamsTag extends TagSupport {
    private String title;

    private boolean showAdvert = true;

    public int doEndTag() throws JspException {
        final ServletRequest request = pageContext.getRequest();
        final PageDetail pageDetail = PageDetail.detailFor(request);
        pageDetail.setTitle(title);
        pageDetail.setShowAdvert(showAdvert);
        return EVAL_PAGE;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setShowAdvert(final boolean showAdvert) {
        this.showAdvert = showAdvert;
    }
}
