package com.nraynaud.sport.web.converter;

import com.nraynaud.sport.formatting.DistanceIO;
import com.opensymphony.xwork2.util.TypeConversionException;
import org.apache.struts2.util.StrutsTypeConverter;

import java.text.ParseException;
import java.util.Map;

@SuppressWarnings({"RawUseOfParameterizedType"})
public class DistanceConverter extends StrutsTypeConverter {

    public Object convertFromString(final Map context, final String[] values, final Class toClass) {
        final String input = values[0];
        try {
            return DistanceIO.parseDistance(input);
        } catch (ParseException e) {
            throw new TypeConversionException(e);
        }
    }

    public String convertToString(final Map context, final Object o) {
        if (o == null)
            return "";
        return DistanceIO.formatDistance((Double) o);
    }
}
