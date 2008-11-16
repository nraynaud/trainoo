package com.nraynaud.sport.web.converter;

import com.nraynaud.sport.formatting.EnergyIO;
import com.opensymphony.xwork2.util.TypeConversionException;
import org.apache.struts2.util.StrutsTypeConverter;

import java.text.ParseException;
import java.util.Map;

public class EnergyConverter extends StrutsTypeConverter {
    @SuppressWarnings({"RawUseOfParameterizedType"})
    public Object convertFromString(final Map context, final String[] values, final Class toClass) {
        final String input = values[0];
        try {
            return EnergyIO.parseEnergy(input);
        } catch (ParseException e) {
            throw new TypeConversionException(e);
        }
    }

    @SuppressWarnings({"RawUseOfParameterizedType"})
    public String convertToString(final Map context, final Object o) {
        if (o == null)
            return "";
        return EnergyIO.formatEnergy((Long) o);
    }
}
