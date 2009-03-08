package com.nraynaud.sport.web.action.workout;

import com.nraynaud.sport.importer.Importer;
import com.nraynaud.sport.web.Public;
import static com.opensymphony.xwork2.Action.SUCCESS;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.Result;
import org.apache.struts2.dispatcher.StreamResult;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

@Result(value = "inputStream", type = StreamResult.class, params = {"contentType", "image/png"})
@Public
public class NikeGraphAction {
    final Importer importer;
    public String userId;
    public String workoutId;

    public NikeGraphAction(final Importer importer) {
        this.importer = importer;
    }

    @SuppressWarnings({"MethodMayBeStatic"})
    public String index() {
        return SUCCESS;
    }

    public InputStream getInputStream() {
        try {
            final URL logo = ServletActionContext.getServletContext().getResource("/static/pimp/logo.png");
            return new ByteArrayInputStream(importer.getPNGImage(userId, workoutId, logo));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
