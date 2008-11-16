package com.nraynaud.sport.web.converter;

import com.nraynaud.sport.formatting.DateIO;
import com.opensymphony.xwork2.util.TypeConversionException;
import org.apache.struts2.util.StrutsTypeConverter;
import org.joda.time.DateTime;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

public class DateConverter extends StrutsTypeConverter {

    @SuppressWarnings({"RawUseOfParameterizedType"})
    public Object convertFromString(final Map context, final String[] values, final Class toClass) {
        final String source = values[0];
        try {
            return DateIO.parseDate(source);
        } catch (ParseException e) {
            throw new TypeConversionException(e);
        }
    }

    @SuppressWarnings({"RawUseOfParameterizedType"})
    public String convertToString(final Map context, final Object o) {
        if (o instanceof Date) {
            return DateIO.DATE_FORMATTER.print(new DateTime(o));
        }
        return "";
    }
}