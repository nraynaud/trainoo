package com.nraynaud.sport.web.action.workout;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.web.ActionDetail;
import com.nraynaud.sport.web.ChainBackCapable;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.actionsupport.AbstractWorkoutAction;
import com.nraynaud.sport.web.result.ChainBack;
import com.nraynaud.sport.web.result.RedirectBack;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.conversion.annotations.Conversion;
import com.opensymphony.xwork2.validator.annotations.Validation;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

import java.util.Date;

@Conversion
@Results({
@Result(name = INPUT, type = ChainBack.class, value = "/WEB-INF/pages/workout/create.jsp"),
@Result(name = SUCCESS, type = RedirectBack.class, params = {"namespace", "/"}, value = "workouts")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
@Validation
public class CreateAction extends AbstractWorkoutAction implements ChainBackCapable {

    public String fromAction;

    public CreateAction(final Application application) {
        super(application);
    }

    @PostOnly
    public String create() {
        application.createWorkout(getDate(), getUser(), getDuration(), getDistance(), getDiscipline());
        return SUCCESS;
    }

    public void validate() {
        super.validate();
        if (getDate().compareTo(new Date()) > 0)
            addFieldError("date", "Cette date est dans le futur");
    }

    public ActionDetail getFromAction() {
        return new ActionDetail(fromAction);
    }
}
