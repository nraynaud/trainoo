package com.nraynaud.sport.web.action.track;

import com.nraynaud.sport.AccessDeniedException;
import com.nraynaud.sport.Application;
import com.nraynaud.sport.TrackNotFoundException;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import com.nraynaud.sport.web.result.Redirect;
import static com.opensymphony.xwork2.Action.SUCCESS;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

@Results({
@Result(name = SUCCESS, type = Redirect.class, params = {"namespace", "/track"}, value = "")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class DeleteAction extends DefaultAction {
    public Long id;

    public DeleteAction(final Application application) {
        super(application);
    }

    @PostOnly
    public String create() {
        try {
            application.deleteTrack(getUser(), id);
        } catch (TrackNotFoundException e) {
            throw new RuntimeException(e);
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }
        return SUCCESS;
    }
}
