package com.nraynaud.sport.web.view;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

public class LayoutParamsTag extends TagSupport {
    private PageDetail pageDetail;

    public void setPageContext(final PageContext pageContext) {
        super.setPageContext(pageContext);
        pageDetail = PageDetail.detailFor(pageContext.getRequest());
    }

    public void setTitle(final String title) {
        pageDetail.setTitle(title);
    }

    public void setShowTitleInPage(final boolean showTitleInPage) {
        pageDetail.setShowTitle(showTitleInPage);
    }

    public void setShowFooter(final boolean showFooter) {
        pageDetail.setShowFooter(showFooter);
    }

    public void setShowHeader(final boolean showHeader) {
        pageDetail.setShowHeader(showHeader);
    }
}
