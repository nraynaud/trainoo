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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Results({
@Result(type = StreamResult.class, value = "tileData.inputStream",
        params = {"contentType", "${tileData.mimeType}", "contentLength", "${tileData.length}"}),
@Result(name = "fromCache", value = "304", type = HttpHeaderResult.class)
        })
public class GetTileAction extends DefaultAction implements ServletResponseAware, ServletRequestAware {
    public int x;
    public int y;
    public int z;
    public String p;
    public String s;
    public TileFetcher.TileData tileData;
    private final TileFetcher tileFetcher;
    private HttpServletResponse response;
    private HttpServletRequest request;

    public GetTileAction(final Application application, final TileFetcher tileFetcher) {
        super(application);
        this.tileFetcher = tileFetcher;
    }

    @SuppressWarnings({"MethodMayBeStatic"})
    @SkipValidation
    public String index() {
        if (request.getHeader("If-None-Match") != null || request.getHeader("If-Modified-Since") != null)
            return "fromCache";
        tileData = tileFetcher.fetchTile(p, z, x, y, s);
        response.setDateHeader("Expires", new DateTime().plusYears(1).getMillis());
        response.setHeader("Etag", "\"" + p + '-' + z + '-' + x + '-' + y + '-' + s + "-" + tileData.length + "\"");
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
