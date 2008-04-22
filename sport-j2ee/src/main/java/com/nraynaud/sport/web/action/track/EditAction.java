package com.nraynaud.sport.web.action.track;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.Track;
import com.nraynaud.sport.TrackNotFoundException;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.PostOnly;
import static com.nraynaud.sport.web.action.track.CreateAction.ensurePointFormat;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import com.nraynaud.sport.web.result.Redirect;
import static com.opensymphony.xwork2.Action.SUCCESS;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;

@Results({
@Result(name = SUCCESS, value = "/WEB-INF/pages/track/edit.jsp", params = {"id", "%{id}"}),
@Result(name = "saved", type = Redirect.class, params = {"namespace", "/track", "id", "${id}"}, value = "")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class EditAction extends DefaultAction {
    public long id;
    private Track track;
    public String title;
    public String points;

    public EditAction(final Application application) {
        super(application);
    }

    @SuppressWarnings({"MethodMayBeStatic"})
    @SkipValidation
    public String index() {
        try {
            track = application.fetchTrack(id);
            return SUCCESS;
        } catch (TrackNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @PostOnly
    public String create() {
        try {
            ensurePointFormat(points);
            application.updateTrack(id, title, points);
            return "saved";
        } catch (TrackNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Track getTrack() {
        return track;
    }
}
