package com.nraynaud.sport.web.action;

import com.nraynaud.sport.importer.Importer;
import com.nraynaud.sport.web.Public;
import com.nraynaud.sport.web.result.Redirect;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.apache.struts2.dispatcher.StreamResult;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Results({
    @Result(name = INPUT, type = ServletDispatcherResult.class, value = "/WEB-INF/pages/workout/nikeWidgetForm.jsp"),
    @Result(name = SUCCESS, value = "inputStream", type = StreamResult.class, params = {"contentType", "image/png"}),
    @Result(name = "url", type = Redirect.class, value = "nike",
            params = {"namespace", "/", "userId", "${userId}", "workoutId", "${workoutId}"})
        })
@Public
public class NikeAction {
    final Importer importer;
    public String userId;
    public String workoutId;
    public String url;
    private ByteArrayInputStream stream;

    public NikeAction(final Importer importer) {
        this.importer = importer;
    }

    @SuppressWarnings({"MethodMayBeStatic"})
    public String index() {
        if (url != null) {
            final Pattern pattern = Pattern.compile(
                    "http://nikeplus.nike.com/nikeplus/\\?l=runners,runs,([0-9]+),runID,([0-9]+)");
            final Matcher matcher = pattern.matcher(url);
            if (!matcher.matches())
                throw new RuntimeException();
            userId = matcher.group(1);
            workoutId = matcher.group(2);
            return "url";
        } else if (userId == null && workoutId == null)
            return INPUT;
        stream = new ByteArrayInputStream(importer.getPNGImage(userId, workoutId));
        return SUCCESS;
    }

    public InputStream getInputStream() {
        return stream;
    }
}
