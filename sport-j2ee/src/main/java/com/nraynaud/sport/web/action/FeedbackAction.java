package com.nraynaud.sport.web.action;

import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.Public;
import com.nraynaud.sport.web.WorkoutDateConverter;
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
        final WorkoutDateConverter converter = new WorkoutDateConverter();
        try {
            final Date date = (Date) converter.convertFromString(null, new String[]{data}, Date.class);
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
