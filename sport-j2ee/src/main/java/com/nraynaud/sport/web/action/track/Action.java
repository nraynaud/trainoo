package com.nraynaud.sport.web.action.track;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.Track;
import com.nraynaud.sport.TrackNotFoundException;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import static com.opensymphony.xwork2.Action.SUCCESS;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;

import java.util.List;

@Results({
@Result(name = SUCCESS, value = "/WEB-INF/pages/track/view.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class Action extends DefaultAction {
    public Long id;
    private Track track;
    private List<Track> tracks;

    public Action(final Application application) {
        super(application);
    }

    @SuppressWarnings({"MethodMayBeStatic"})
    @SkipValidation
    public String index() {
        try {
            if (id != null)
                track = application.fetchTrack(id.longValue());
            tracks = application.fetchTracks();
            return SUCCESS;
        } catch (TrackNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Track getTrack() {
        return track;
    }

    public List<Track> getTracks() {
        return tracks;
    }
}
