package com.nraynaud.sport.web.action.track;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.geography.TileFetcher;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import org.apache.struts2.config.Result;
import org.apache.struts2.dispatcher.StreamResult;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.util.Locale;

@Result(type = StreamResult.class, value = "inputStream",
        params = {"contentType", "image/jpeg", "contentLength", "${data.length}"})
public class GetTileAction extends DefaultAction implements ServletResponseAware {
    public String tileName;
    private final TileFetcher tileFetcher;
    public byte[] data;
    public ByteArrayInputStream inputStream;
    private HttpServletResponse response;

    public GetTileAction(final Application application, final TileFetcher tileFetcher) {
        super(application);
        this.tileFetcher = tileFetcher;
    }

    @SuppressWarnings({"MethodMayBeStatic"})
    @SkipValidation
    public String index() {
        if (!tileName.matches("[a-zA-Z0-9.]+"))
            throw new RuntimeException("strange tilename: " + tileName);
        data = tileFetcher.fetchTile(tileName);
        inputStream = new ByteArrayInputStream(data);
        // Wed, 09 Apr 2008 23:47:48 GMT
        response.setHeader("Expires",
                DateTimeFormat.forPattern("E',' dd MMM yyyy kk:mm:ss 'GMT'").withLocale(Locale.US).print(
                        new DateTime().plusYears(1)));
        //max-age=86400
        //response.setHeader("Cache-Control");
        response.setHeader("Etag", "\"" + tileName + "-" + data.length + "\"");
        return SUCCESS;
    }

    public void setServletResponse(final HttpServletResponse response) {
        this.response = response;
    }
}
