package com.nraynaud.sport.web.action;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.data.GlobalWorkoutsPageData;
import com.nraynaud.sport.syndication.FeedWriter;
import com.nraynaud.sport.web.Public;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import com.opensymphony.xwork2.mock.MockResult;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.validation.SkipValidation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Results({
    @Result(type = MockResult.class, value = "")
        })
@Public
public class SyndicationAction extends DefaultAction implements ServletRequestAware, ServletResponseAware {
    private static final String FEED_TYPE = "type";
    private static final String MIME_TYPE = "application/xml; charset=UTF-8";

    private HttpServletRequest request;
    private HttpServletResponse response;
    private final FeedWriter feedWriter;

    public SyndicationAction(final Application application, final FeedWriter feedWriter) {
        super(application);
        this.feedWriter = feedWriter;
    }

    @SuppressWarnings({"MethodMayBeStatic"})
    @SkipValidation
    public String index() {
        try {
            final GlobalWorkoutsPageData workoutsPageData = application.fetchFrontPageData(0, 20,
                    Collections.<String>emptyList());
            String feedType = request.getParameter(FEED_TYPE);
            System.out.println("feed type (requested): " + feedType);
            feedType = feedType != null ? feedType : "atom_1.0";
            response.setContentType(MIME_TYPE);
            feedWriter.writeOn(workoutsPageData, response.getWriter(), feedType);
            return SUCCESS;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setServletRequest(final HttpServletRequest request) {
        this.request = request;
    }

    public void setServletResponse(final HttpServletResponse response) {
        this.response = response;
    }
}
