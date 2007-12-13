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

@Conversion
@Results({
@Result(name = INPUT, value = "/WEB-INF/pages/editWorkout.jsp"),
@Result(name = SUCCESS, type = Redirect.class, value = "workouts"),
@Result(name = "workouts-redirect", type = Redirect.class, value = "workouts")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
@Validation
public class EditWorkoutAction extends AbstractWorkoutAction {

    private Long id;

    public EditWorkoutAction(final Application app) {
        super(app);
    }


    public void setId(final Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }

    @PostOnly
    public String update() {
        if (id != null) {
            final Workout workout = application.getWorkout(id, user);
            if (workout != null) {
                application.updateWorkout(workout, getDate(), getDuration(), getDistance(), getDiscipline());
                return "workouts-redirect";
            } else {
                addActionError("l'entraînement désigné n'existe pas");
                return INPUT;
            }
        } else
            return "workouts-redirect";
    }

    @SkipValidation
    public String index() {
        if (id != null) {
            final Workout workout = application.getWorkout(id, user);
            if (workout != null) {
                this.setDate(workout.getDate());
                this.setDistance(workout.getDistance());
                this.setDuration(workout.getDuration());
                this.setDiscipline(workout.getDiscipline());
                return INPUT;
            } else {
                addActionError("l'entraînement désigné n'existe pas");
                return INPUT;
            }
        } else
            return "workouts-redirect";
    }
}
