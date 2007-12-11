package com.nraynaud.sport.web.action;

import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.Public;
import com.nraynaud.sport.web.converter.DateConverter;
import com.nraynaud.sport.web.converter.DistanceConverter;
import com.nraynaud.sport.web.converter.DurationConverter;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.dispatcher.ServletDispatcherResult;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

@Result(type = ServletDispatcherResult.class, name = Constants.FEEDBACK, value = "/WEB-INF/pages/feedback.jsp")
@ParentPackage(Constants.STRUTS_PACKAGE)
@Public
public class FeedbackAction {
    private static final ThreadLocal<DateFormat> LONG_DATEFORMAT = new ThreadLocal<DateFormat>() {
        protected DateFormat initialValue() {
            return DateFormat.getDateInstance(DateFormat.FULL, Locale.FRANCE);
        }
    };


    private String data;
    private String type;

    @SuppressWarnings({"MethodMayBeStatic"})
    public String create() {
        return Constants.FEEDBACK;
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
            return LONG_DATEFORMAT.get().format(date);
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
