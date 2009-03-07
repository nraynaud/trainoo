package com.nraynaud.sport.web.action.workout;

import com.nraynaud.sport.importer.Importer;
import com.nraynaud.sport.web.Public;
import static com.opensymphony.xwork2.Action.SUCCESS;
import org.apache.struts2.config.Result;
import org.apache.struts2.dispatcher.StreamResult;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

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
        final byte[] bytes = importer.getPNGImage(userId, workoutId);
        return new ByteArrayInputStream(bytes);
    }
}
