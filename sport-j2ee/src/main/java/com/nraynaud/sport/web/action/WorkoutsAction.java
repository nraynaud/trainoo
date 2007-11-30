package com.nraynaud.sport.web.action;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.User;
import com.nraynaud.sport.Workout;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.DefaultAction;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.result.Redirect;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.conversion.annotations.Conversion;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.interceptor.validation.SkipValidation;

import java.util.Date;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"MethodMayBeStatic"})
@Conversion
@Results({
@Result(name = SUCCESS, value = "/WEB-INF/pages/personalWorkoutList.jsp"),
@Result(name = "edit", value = "/WEB-INF/pages/editWorkout.jsp"),
@Result(name = INPUT, value = "/WEB-INF/pages/personalWorkoutList.jsp"),
@Result(name = "added", type = Redirect.class, value = "workouts")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class WorkoutsAction extends DefaultAction implements SessionAware {

    private final Application application;

    private Long id;
    private Date date = new Date();
    private Long duration;
    private Double distance;
    private User user;

    public WorkoutsAction(final Application app) {
        this.application = app;
    }

    @TypeConversion(converter = "com.nraynaud.sport.web.converter.DateConverter")
    public Date getNewDate() {
        return date;
    }

    public void setNewDate(final Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }


    public String getUserName() {
        return getUser().getName();
    }

    public List<Workout> getWorkouts() {
        return application.getWorkoutsForUser(getUser());
    }

    @SkipValidation
    public String index() {
        return SUCCESS;
    }

    @SkipValidation
    public String edit() {
        if (id != null) {
            final Workout workout = application.getWorkout(id, user);
            if (workout != null) {
                this.setNewDate(workout.getDate());
                this.setDistance(workout.getDistance());
                this.setDuration(workout.getDuration());
                return "edit";
            } else {
                addActionError("l'entraînement désigné n'existe pas");
                return INPUT;
            }
        } else
            return SUCCESS;
    }

    @PostOnly
    public String create() {
        application.createWorkout(getNewDate(), getUser(), getDuration(), getDistance());
        return "added";
    }

    @SuppressWarnings({"RawUseOfParameterizedType"})
    public void setSession(final Map session) {
        this.user = (User) session.get("user");
    }

    public Long getDuration() {
        return duration;
    }

    @TypeConversion(converter = "com.nraynaud.sport.web.converter.DurationConverter")
    public void setDuration(final Long duration) {
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Double getDistance() {
        return distance;
    }

    @TypeConversion(converter = "com.nraynaud.sport.web.converter.DistanceConverter")
    public void setDistance(final Double distance) {
        this.distance = distance;
    }
}
