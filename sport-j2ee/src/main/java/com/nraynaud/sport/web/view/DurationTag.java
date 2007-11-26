package com.nraynaud.sport.web.view;

import static com.nraynaud.sport.web.WorkoutDurationConverter.formatDuration;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.views.jsp.TagUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

public class DurationTag extends TagSupport {
    private String name;

    public int doStartTag() throws JspException {
        try {
            final ValueStack stack = TagUtils.getStack(pageContext);
            final Long duration = (Long) stack.findValue(name);
            if (duration != null)
                pageContext.getOut().write(formatDuration(duration));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return SKIP_BODY;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
