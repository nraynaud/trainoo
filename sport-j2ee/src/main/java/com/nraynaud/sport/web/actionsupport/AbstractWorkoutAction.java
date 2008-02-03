package com.nraynaud.sport.web.actionsupport;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.web.SportRequest;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;

import java.util.Date;

public class AbstractWorkoutAction extends DefaultAction {
    private Date date = new Date();
    private Long duration;
    private Double distance;
    private String discipline;

    public AbstractWorkoutAction(final Application application) {
        super(application);
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

    public SportRequest getRequest() {
        return request;
    }
}
