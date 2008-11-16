package com.nraynaud.sport.web.action;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.User;
import com.nraynaud.sport.formatting.DateIO;
import com.nraynaud.sport.formatting.DistanceIO;
import com.nraynaud.sport.formatting.DurationIO;
import com.nraynaud.sport.formatting.EnergyIO;
import com.nraynaud.sport.web.Constants;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletDispatcherResult;

import java.util.List;

@Results({
        //the type avoids having the page decorated by application.jsp
    @Result(name = Constants.FEEDBACK, type = ServletDispatcherResult.class, value = "/WEB-INF/pages/feedback.jsp"),
    @Result(name = "logins", type = ServletDispatcherResult.class, value = "/WEB-INF/pages/logins.jsp"),
    @Result(name = "participants", type = ServletDispatcherResult.class, value = "/WEB-INF/pages/participants.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class FeedbackAction {

    private String data;
    private String type;
    private long workout;

    private final Application application;

    public FeedbackAction(final Application application) {
        this.application = application;
    }

    @SuppressWarnings({"MethodMayBeStatic"})
    public String create() {
        return "logins".equals(type) ? "logins" : "participants".equals(type) ? "participants" : Constants.FEEDBACK;
    }

    public String getResult() {
        if ("date".equals(type)) {
            return convertDate();
        } else if ("duration".equals(type)) {
            return convertDuration();
        } else if ("distance".equals(type)) {
            return convertDistance();
        } else if ("energy".equals(type)) {
            return convertEnergy();
        }
        return "";
    }

    public List<String> getLogins() {
        return application.fechLoginBeginningBy(data);
    }

    public List<User> getParticipants() {
        return application.fetchUsersBeginningByAndAddableToWorkout(data, workout);
    }

    private String convertDistance() {
        try {
            final Double distance = DistanceIO.parseDistance(data);
            return DistanceIO.formatDistance(distance) + " kilomètre(s)";
        } catch (Exception e) {
            return "La distance n'a pas été comprise.";
        }
    }

    private String convertDuration() {
        try {
            final Long globalSeconds = DurationIO.parseDuration(data);
            return DurationIO.formatDuration(globalSeconds, " heure(s) ", " minute(s) ", " seconde(s)");
        } catch (Exception e) {
            return "La durée n'a pas été comprise.";
        }
    }

    private String convertDate() {
        try {
            return DateIO.parseAndPrettyPrint(data);
        } catch (Exception e) {
            return "La date n'a pas été comprise.";
        }
    }

    private String convertEnergy() {
        try {
            final Long energy = EnergyIO.parseEnergy(data);
            return EnergyIO.formatEnergy(energy) + "kcal";
        } catch (Exception e) {
            return "L'énergie dépensée n'a pas été comprise.";
        }
    }

    public String getData() {
        return data;
    }

    public void setData(final String data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public long getWorkout() {
        return workout;
    }

    public void setWorkout(final long workout) {
        this.workout = workout;
    }
}
