package com.nraynaud.sport.web;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.User;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import org.apache.struts2.interceptor.ServletRequestAware;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class AbstractWorkoutAction extends DefaultAction implements ServletRequestAware {
    protected final Application application;
    private Date date = new Date();
    private Long duration;
    private Double distance;
    private String discipline;
    protected User user;

    public AbstractWorkoutAction(
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

    public void setServletRequest(final HttpServletRequest request) {
        this.user = SportSession.fromRequest(request).getUser();
    }
}
