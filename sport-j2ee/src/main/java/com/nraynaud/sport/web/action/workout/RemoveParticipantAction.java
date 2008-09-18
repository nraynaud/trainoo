package com.nraynaud.sport.web.action.workout;

import com.nraynaud.sport.*;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import com.nraynaud.sport.web.result.Redirect;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionChainResult;
import com.opensymphony.xwork2.conversion.annotations.Conversion;
import com.opensymphony.xwork2.validator.annotations.Validation;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletDispatcherResult;

import java.util.ArrayList;
import java.util.Collection;

@Conversion
@Results({
@Result(name = INPUT, type = ServletDispatcherResult.class, value = "/WEB-INF/pages/feedback.jsp"),
@Result(name = SUCCESS, type = ServletDispatcherResult.class, value = "/WEB-INF/pages/feedback.jsp")
})
@ParentPackage(Constants.STRUTS_PACKAGE)
@Validation
public class RemoveParticipantAction extends DefaultAction {
    public Long id;
    public String participant;
    private String result;

    public RemoveParticipantAction(final Application application) {
        super(application);
    }

    public String getResult() {
        return result;
    }

    public String create() {
        try {
            application.removeWorkoutParticipants(getUser(), id, new String[] {participant});
            result = "success";
            return SUCCESS;
        } catch (AccessDeniedException e) {
            result = "failure";
            return INPUT;
        }
    }
}
