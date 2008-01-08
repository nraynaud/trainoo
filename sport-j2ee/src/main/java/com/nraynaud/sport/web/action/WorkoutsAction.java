package com.nraynaud.sport.web.action;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.Workout;
import com.nraynaud.sport.web.AbstractWorkoutAction;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.result.Redirect;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.conversion.annotations.Conversion;
import com.opensymphony.xwork2.validator.annotations.Validation;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;

import java.util.List;

@Conversion
@Results({
@Result(name = SUCCESS, value = "/WEB-INF/pages/personalWorkoutList.jsp"),
@Result(name = INPUT, value = "/WEB-INF/pages/personalWorkoutList.jsp"),
@Result(name = "workouts-redirect", type = Redirect.class, value = "workouts")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
@Validation
public class WorkoutsAction extends AbstractWorkoutAction {
    private final Application application;

    public WorkoutsAction(final Application application) {
        this.application = application;
    }

    public String getUserName() {
        return getUser().getName();
    }

    public List<Workout> getWorkouts() {
        return application.getWorkoutsForUser(getUser(), 10);
    }

    @SkipValidation
    public String index() {
        return SUCCESS;
    }

    @PostOnly
    public String create() {
        application.createWorkout(getDate(), getUser(), getDuration(), getDistance(), getDiscipline());
        return "workouts-redirect";
    }
}
