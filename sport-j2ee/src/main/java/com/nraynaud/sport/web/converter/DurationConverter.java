package com.nraynaud.sport.web.converter;

import com.nraynaud.sport.formatting.DurationIO;
import com.opensymphony.xwork2.util.TypeConversionException;
import org.apache.struts2.util.StrutsTypeConverter;

import java.util.Map;

@SuppressWarnings({"RawUseOfParameterizedType"})
public class DurationConverter extends StrutsTypeConverter {

    public Object convertFromString(final Map context, final String[] values, final Class toClass) {
        try {
            final String input = values[0];
            return input.length() == 0 ? null : DurationIO.parseDuration(input);
        } catch (IllegalArgumentException e) {
            throw new TypeConversionException(e);
        }
    }

    public String convertToString(final Map context, final Object o) {
        if (o == null)
            return "";
        return DurationIO.formatDuration((Long) o, "h", "\'", "''");
    }
}
