package com.nraynaud.sport.web.action;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.Workout;
import com.nraynaud.sport.data.GlobalWorkoutsPageData;
import com.nraynaud.sport.web.Public;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import com.opensymphony.xwork2.mock.MockResult;
import com.sun.syndication.feed.synd.*;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.validation.SkipValidation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Results({
    @Result(type = MockResult.class, value = "")
        })
@Public
public class SyndicationAction extends DefaultAction implements ServletRequestAware, ServletResponseAware {
    private static final String FEED_TYPE = "type";
    private static final String MIME_TYPE = "application/xml; charset=UTF-8";

    private HttpServletRequest request;
    private HttpServletResponse result;

    public SyndicationAction(final Application application) {
        super(application);
    }

    @SuppressWarnings({"MethodMayBeStatic"})
    @SkipValidation
    public String index() {
        try {
            try {
                final SyndFeed feed = getFeed(application);
                String feedType = request.getParameter(FEED_TYPE);
                System.out.println("feed type (requested): " + feedType);
                feedType = feedType != null ? feedType : "atom_0.3";
                feed.setFeedType(feedType);
                result.setContentType(MIME_TYPE);
                final SyndFeedOutput output = new SyndFeedOutput();
                output.output(feed, result.getWriter());
            }
            catch (FeedException ex) {
                result.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Could not generate feed");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return SUCCESS;
    }

    private static SyndFeed getFeed(final Application application) {
        final SyndFeed feed = new SyndFeedImpl();
        feed.setTitle("Trainoo.com : les derniers entraînements");
        feed.setLink("http://trainoo.com/");
        feed.setDescription("Les derniers entraînements sur Trainoo.com");
        final List<SyndEntry> entries = new ArrayList<SyndEntry>();
        final GlobalWorkoutsPageData workoutsPageData = application.fetchFrontPageData(0, 20,
                Collections.<String>emptyList());
        for (final Workout workout : workoutsPageData.workoutsData.workouts) {
            final SyndEntry entry = new SyndEntryImpl();
            entry.setTitle(workout.getDiscipline().toString() + " " + workout.getUser().getName());
            entry.setLink("http://trainoo.com/workout/?id=" + workout.getId());
            entry.setPublishedDate(workout.getDate());
            final SyndContent description = new SyndContentImpl();
            description.setType("text/plain");
            description.setValue(String.valueOf(workout.getComment()));
            entries.add(entry);
        }
        feed.setEntries(entries);
        return feed;
    }

    public void setServletRequest(final HttpServletRequest request) {
        this.request = request;
    }

    public void setServletResponse(final HttpServletResponse response) {
        result = response;
    }
}
