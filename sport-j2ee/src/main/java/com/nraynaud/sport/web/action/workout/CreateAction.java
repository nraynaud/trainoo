package com.nraynaud.sport.web.action.workout;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.web.AbstractWorkoutAction;
import com.nraynaud.sport.web.ChainBackCapable;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.result.ChainBack;
import com.nraynaud.sport.web.result.RedirectBack;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.conversion.annotations.Conversion;
import com.opensymphony.xwork2.validator.annotations.Validation;
import org.apache.struts2.config.Namespace;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

@Conversion
@Results({
@Result(name = INPUT, type = ChainBack.class, value = "/WEB-INF/pages/workout/create.jsp"),
@Result(name = SUCCESS, type = RedirectBack.class, params = {"namespace", "/"}, value = "workouts")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
@Namespace("/workout")
@Validation
public class CreateAction extends AbstractWorkoutAction implements ChainBackCapable {

    private final Application application;

    private String fromAction;

    public CreateAction(final Application application) {
        this.application = application;
    }

    @PostOnly
    public String create() {
        application.createWorkout(getDate(), getUser(), getDuration(), getDistance(), getDiscipline());
        return SUCCESS;
    }

    public String getFromAction() {
        return fromAction;
    }

    public void setFromAction(final String fromAction) {
        this.fromAction = fromAction;
    }
}
