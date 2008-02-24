package com.nraynaud.sport.web.view;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;

public class WriteJavacriptTag extends TagSupport {
    public int doStartTag() throws JspException {
        final PageDetail pageDetail = PageDetail.detailFor(pageContext.getRequest());
        final List<String> javascript = pageDetail.getJavascript();
        if (!javascript.isEmpty()) {
            final JspWriter out = pageContext.getOut();
            try {
                for (final String script : javascript) {
                    out.write("<script type=\"text/javascript\">");
                    out.write(script);
                    out.write("</script>");
                }
            } catch (IOException e) {
                throw new JspException(e);
            }
        }
        return SKIP_BODY;
    }
}
