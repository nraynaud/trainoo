package com.nraynaud.sport.web.action.workout;

import com.nraynaud.sport.Application;
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

@Conversion
@Results({
@Result(name = INPUT, value = "/WEB-INF/pages/workout/edit.jsp"),
@Result(name = SUCCESS, type = Redirect.class, params = {"namespace", "/workout", "id", "${id}"}, value = ""),
@Result(name = "delete", type = ActionChainResult.class, value = "delete",
        params = {"namespace", "/workout", "method", "create"})
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
@Validation
public class ParticipantsAction extends DefaultAction {
    public Long id;
    public String[] participants;

    public ParticipantsAction(final Application application) {
        super(application);
    }

    @PostOnly
    public String create() {
        application.setWorkoutParticipants(getUser(), id, participants);
        return SUCCESS;
    }
}
