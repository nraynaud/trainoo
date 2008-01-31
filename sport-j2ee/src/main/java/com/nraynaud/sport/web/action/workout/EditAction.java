package com.nraynaud.sport.web.action.workout;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.Workout;
import com.nraynaud.sport.WorkoutNotFoundException;
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
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;

import javax.servlet.http.HttpServletRequest;

@Conversion
@Results({
@Result(name = INPUT, value = "/WEB-INF/pages/workout/edit.jsp"),
@Result(name = SUCCESS, type = Redirect.class, params = {"namespace", "/"}, value = "workouts")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
@Validation
public class EditAction extends AbstractWorkoutAction implements ServletRequestAware {
    private Long id;

    private final Application application;
    private boolean delete;

    public EditAction(final Application application) {
        this.application = application;
    }

    @SkipValidation
    public String index() {
        if (id != null) {
            final Workout workout;
            try {
                workout = application.fetchWorkoutAndCheckUser(id, getUser(), true);
                setDate(workout.getDate());
                setDistance(workout.getDistance());
                setDuration(workout.getDuration());
                setDiscipline(workout.getDiscipline());
                return INPUT;
            } catch (WorkoutNotFoundException e) {
                addActionError("l'entraînement désigné n'existe pas");
                return INPUT;
            }
        } else
            return SUCCESS;
    }

    @PostOnly
    public String create() {
        if (id != null) {
            try {
                if (delete)
                    application.deleteWorkout(id, getUser());
                else
                    application.updateWorkout(id, getUser(), getDate(), getDuration(), getDistance(), getDiscipline());
                return SUCCESS;
            } catch (WorkoutNotFoundException e) {
                addActionError("l'entraînement désigné n'existe pas pour cet utilisateur");
                return INPUT;
            }
        } else
            return SUCCESS;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setServletRequest(final HttpServletRequest request) {
        delete = request.getParameter("delete") != null;
    }
}
