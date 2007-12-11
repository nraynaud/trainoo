package com.nraynaud.sport.web;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.User;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Date;
import java.util.Map;

public class WorkoutAction extends DefaultAction implements SessionAware {
    protected final Application application;
    private Date date = new Date();
    private Long duration;
    private Double distance;
    private String discipline;
    protected User user;

    public WorkoutAction(
            final Application app) {
        application = app;
    }

    @TypeConversion(converter = "com.nraynaud.sport.web.converter.DateConverter")
    public void setDate(final Date date) {
        this.date = date;
    }

    @TypeConversion(converter = "com.nraynaud.sport.web.converter.DurationConverter")
    public void setDuration(final Long duration) {
        this.duration = duration;
    }

    @TypeConversion(converter = "com.nraynaud.sport.web.converter.DistanceConverter")
    public void setDistance(final Double distance) {
        this.distance = distance;
    }

    @RequiredStringValidator(message = "Vous avez oublié la discipline")
    public void setDiscipline(final String discipline) {
        this.discipline = discipline;
    }

    public Date getDate() {
        return date;
    }

    public Long getDuration() {
        return duration;
    }

    public Double getDistance() {
        return distance;
    }

    public String getDiscipline() {
        return discipline;
    }

    @SuppressWarnings({"RawUseOfParameterizedType"})
    public void setSession(final Map session) {
        this.user = (User) session.get("user");
    }
}
