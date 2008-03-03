package com.nraynaud.sport.web.view;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class LayoutParamsTag extends TagSupport {
    private String title;
    private boolean showTitleInPage = true;

    public int doEndTag() throws JspException {
        final ServletRequest request = pageContext.getRequest();
        final PageDetail pageDetail = PageDetail.detailFor(request);
        pageDetail.setTitle(title);
        pageDetail.setShowTitle(showTitleInPage);
        return EVAL_PAGE;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setShowTitleInPage(boolean showTitleInPage) {
        this.showTitleInPage = showTitleInPage;
    }
}
