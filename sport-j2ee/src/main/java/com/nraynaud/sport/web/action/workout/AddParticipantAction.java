package com.nraynaud.sport.web.action.workout;

import com.nraynaud.sport.AccessDeniedException;
import com.nraynaud.sport.Application;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletDispatcherResult;

@Results({
    @Result(name = INPUT, type = ServletDispatcherResult.class, value = "/WEB-INF/pages/feedback.jsp"),
    @Result(name = SUCCESS, type = ServletDispatcherResult.class, value = "/WEB-INF/pages/feedback.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class AddParticipantAction extends DefaultAction {
    public Long id;
    public String participant;
    private String result;

    public AddParticipantAction(final Application application) {
        super(application);
    }

    public String getResult() {
        return result;
    }

    @PostOnly
    public String create() {
        try {
            application.addWorkoutParticipants(getUser(), id, new String[]{participant});
            result = "success";
            return SUCCESS;
        } catch (AccessDeniedException e) {
            result = "failure";
            return INPUT;
        }
    }
}
