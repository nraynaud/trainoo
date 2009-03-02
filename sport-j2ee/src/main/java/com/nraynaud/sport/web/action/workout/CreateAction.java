package com.nraynaud.sport.web.action.workout;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.Workout;
import com.nraynaud.sport.web.ActionDetail;
import com.nraynaud.sport.web.ChainBackCapable;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.actionsupport.AbstractWorkoutAction;
import com.nraynaud.sport.web.result.Redirect;
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
    @Result(name = INPUT, value = "/WEB-INF/pages/workout/edit.jsp"),
    @Result(name = SUCCESS, type = Redirect.class, params = {"namespace", "/workout", "id", "${newId}"}, value = "")
        })
@Validation
@ParentPackage(Constants.STRUTS_PACKAGE)
public class CreateAction extends AbstractWorkoutAction implements ChainBackCapable {

    public String fromAction;
    public String onErrorAction;
    public String newId;

    public CreateAction(final Application application) {
        super(application, NewAction.PAGE_TILE);
    }

    @PostOnly
    public String create() {
        final Workout workout = application.createWorkout(getDate(), getUser(), getDuration(), getDistance(),
                getEnergy(), getDiscipline(),
                getComment(), null);
        newId = workout.getId().toString();
        return SUCCESS;
    }

    public void validate() {
        super.validate();
        if (getDate().compareTo(new Date()) > 0)
            addFieldError("date", "Cette date est dans le futur");
    }

    public ActionDetail getFromAction() {
        return ActionDetail.decodeActionDetail(fromAction);
    }

    public ActionDetail getOnErrorAction() {
        return ActionDetail.decodeActionDetail(onErrorAction);
    }

    protected ActionDetail cancelAction() {
        return NewAction.CANCEL_ACTION;
    }
}
