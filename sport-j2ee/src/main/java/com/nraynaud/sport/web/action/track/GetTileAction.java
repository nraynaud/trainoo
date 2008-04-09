package com.nraynaud.sport.web.action.track;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.geography.TileFetcher;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.HttpHeaderResult;
import org.apache.struts2.dispatcher.StreamResult;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.util.Locale;

@Results({
@Result(type = StreamResult.class, value = "inputStream",
        params = {"contentType", "image/jpeg", "contentLength", "${data.length}"}),
@Result(name = "fromCache", value = "304", type = HttpHeaderResult.class)
        })
public class GetTileAction extends DefaultAction implements ServletResponseAware, ServletRequestAware {
    public String tileName;
    private final TileFetcher tileFetcher;
    public byte[] data;
    public ByteArrayInputStream inputStream;
    private HttpServletResponse response;
    private HttpServletRequest request;
    private static final DateTimeFormatter HTTP_DATE_TIME_FORMATTER = DateTimeFormat.forPattern(
            "E',' dd MMM yyyy kk:mm:ss 'GMT'").withLocale(Locale.US);

    public GetTileAction(final Application application, final TileFetcher tileFetcher) {
        super(application);
        this.tileFetcher = tileFetcher;
    }

    @SuppressWarnings({"MethodMayBeStatic"})
    @SkipValidation
    public String index() {
        if (request.getHeader("If-None-Match") != null || request.getHeader("If-Modified-Since") != null)
            return "fromCache";
        if (!tileName.matches("[a-zA-Z0-9.]+"))
            throw new RuntimeException("strange tilename: " + tileName);
        data = tileFetcher.fetchTile(tileName);
        inputStream = new ByteArrayInputStream(data);
        response.setHeader("Expires", HTTP_DATE_TIME_FORMATTER.print(new DateTime().plusYears(1)));
        response.setHeader("Etag", "\"" + tileName + "-" + data.length + "\"");
        response.setHeader("Cache-control", "max-age=31536000");
        return SUCCESS;
    }

    public void setServletResponse(final HttpServletResponse response) {
        this.response = response;
    }

    public void setServletRequest(final HttpServletRequest request) {
        this.request = request;
    }
}
