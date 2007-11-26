package com.nraynaud.sport.web.action;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.User;
import com.nraynaud.sport.Workout;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.DefaultAction;
import com.nraynaud.sport.web.PostOnly;
import com.opensymphony.xwork2.Action;
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
@Result(name = Action.SUCCESS, value = "/WEB-INF/pages/personalWorkoutList.jsp"),
@Result(name = Action.INPUT, value = "/WEB-INF/pages/personalWorkoutList.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class WorkoutsAction extends DefaultAction implements SessionAware {

    private final Application application;

    private Long id;
    private Date date = new Date();
    private Long duration;
    private User user;

    public WorkoutsAction(final Application app) {
        this.application = app;
    }

    @TypeConversion(converter = "com.nraynaud.sport.web.WorkoutDateConverter")
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
        return Action.SUCCESS;
    }

    @SkipValidation
    public String edit() {
        return Action.SUCCESS;
    }

    @PostOnly
    public String create() {
        application.createWorkout(getNewDate(), getUser(), getDuration());
        return Action.SUCCESS;
    }

    @SuppressWarnings({"RawUseOfParameterizedType"})
    public void setSession(final Map session) {
        this.user = (User) session.get("user");
    }

    public Long getDuration() {
        return duration;
    }

    @TypeConversion(converter = "com.nraynaud.sport.web.WorkoutDurationConverter")
    public void setDuration(final Long duration) {
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }
}
