package com.nraynaud.sport.web.actionsupport;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.web.SportRequest;
import com.nraynaud.sport.web.view.WorkoutView;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

import java.util.Date;
import java.util.Map;

public class AbstractWorkoutAction extends DefaultAction implements ModelDriven<WorkoutView> {
    private Date date = new Date();
    private Long duration;
    private Double distance;
    private Long energy;
    private String discipline;
    private String comment;

    public String id;

    public static final String MAX_COMMENT_LENGTH = "4000";

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

    public void setEnergy(final Long energy) {
        this.energy = energy;
    }

    @RequiredStringValidator(message = "Vous avez oublié la discipline")
    public void setDiscipline(final String discipline) {
        this.discipline = discipline;
    }

    @StringLengthFieldValidator(message = "Le commentaire doit faire moins de ${maxLength} caratères.",
            maxLength = MAX_COMMENT_LENGTH)
    public void setComment(final String comment) {
        this.comment = comment != null && comment.length() > 0 ? comment : null;
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

    public String getComment() {
        return comment;
    }

    public Long getEnergy() {
        return energy;
    }

    public WorkoutView getModel() {
        final WorkoutView interpreted = WorkoutView.createView(id, getDiscipline(), getDate(), getDistance(),
                getDuration(), getEnergy(), getComment());
        return new WorkoutView(id,
                errorOrValue("discipline", interpreted.discipline),
                errorOrValue("date", interpreted.date),
                errorOrValue("distance", interpreted.distance),
                errorOrValue("duration", interpreted.duration),
                errorOrValue("energy", interpreted.energy),
                errorOrValue("comment", interpreted.comment));
    }

    private static String errorOrValue(final String fieldKey, final String value) {
        //noinspection unchecked
        final Map<String, String[]> conversionErrors = ActionContext.getContext().getConversionErrors();
        final String[] fieldValue = conversionErrors.get(fieldKey);
        return fieldValue != null ? fieldValue[0] : value;
    }
}
