package com.nraynaud.sport.web.view;

import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.jsp.TagUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public abstract class FormattingTagSupport<T> extends TagSupport {
    private String name;

    @SuppressWarnings({"unchecked"})
    public int doStartTag() throws JspException {
        try {
            final ValueStack stack = TagUtils.getStack(pageContext);
            final T value = (T) stack.findValue(name);
            if (value != null)
                pageContext.getOut().write(formatValue(value));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return SKIP_BODY;
    }

    protected abstract String formatValue(T value);

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
