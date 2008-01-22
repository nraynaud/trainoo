package com.nraynaud.sport.web.action;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.converter.DateConverter;
import com.nraynaud.sport.web.converter.DistanceConverter;
import com.nraynaud.sport.web.converter.DurationConverter;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletDispatcherResult;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Results({
        //the type avoid having the page decorated by application.jsp
@Result(name = Constants.FEEDBACK, type = ServletDispatcherResult.class, value = "/WEB-INF/pages/feedback.jsp"),
@Result(name = "logins", type = ServletDispatcherResult.class, value = "/WEB-INF/pages/logins.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class FeedbackAction {
    public static final Object DATE_LOCK = new Object();

    private String data;
    private String type;

    private final Application application;

    public FeedbackAction(final Application application) {
        this.application = application;
    }

    @SuppressWarnings({"MethodMayBeStatic"})
    public String create() {
        return "logins".equals(type) ? "logins" : Constants.FEEDBACK;
    }

    public String getResult() {
        if ("date".equals(type)) {
            return convertDate();
        } else if ("duration".equals(type)) {
            return convertDuration();
        } else if ("distance".equals(type)) {
            return convertDistance();
        }
        return "";
    }

    public List<String> getLogins() {
        return application.fechLoginBeginningBy(data);
    }

    private String convertDistance() {
        try {
            final Double distance = DistanceConverter.parseDistance(data);
            return DistanceConverter.formatNumber(distance) + "kilomètre(s)";
        } catch (Exception e) {
            return "La distance n'a pas été comprise.";
        }
    }

    private String convertDuration() {
        try {
            final Long globalSeconds = DurationConverter.parseDuration(data);
            return DurationConverter.formatDuration(globalSeconds, "heures ", "minutes ", "secondes");
        } catch (Exception e) {
            return "La durée n'a pas été comprise.";
        }
    }

    private String convertDate() {
        try {
            final Date date = DateConverter.parseDate(data);
            synchronized (DATE_LOCK) {
                return DateFormat.getDateInstance(DateFormat.FULL, Locale.FRANCE).format(date);
            }
        } catch (Exception e) {
            return "La date n'a pas été comprise.";
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
}
