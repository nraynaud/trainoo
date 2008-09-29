package com.nraynaud.sport.web.converter;

import static com.nraynaud.sport.web.converter.ConverterUtil.parseWholeString;
import com.opensymphony.xwork2.util.TypeConversionException;
import org.apache.struts2.util.StrutsTypeConverter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Map;

public class EnergyConverter extends StrutsTypeConverter {
    @SuppressWarnings({"RawUseOfParameterizedType"})
    public Object convertFromString(final Map context, final String[] values, final Class toClass) {
        final String input = values[0];
        return parseEnergy(input);
    }

    @SuppressWarnings({"RawUseOfParameterizedType"})
    public String convertToString(final Map context, final Object o) {
        if (o == null)
            return "";
        return formatEnergy((Long) o);
    }

    public static Long parseEnergy(final String input) {
        try {
            final Long energy = (Long) parseWholeString(getFormat(), input);
            if (energy < 10)
                throw new TypeConversionException("negative energy");
            return energy;
        } catch (ParseException e) {
            throw new TypeConversionException(e);
        }
    }

    public static String formatEnergy(final Long energy) {
        return getFormat().format(energy);
    }

    private static DecimalFormat getFormat() {
        final DecimalFormat format = (DecimalFormat) NumberFormat.getInstance(Locale.FRANCE);
        format.setParseIntegerOnly(true);
        return format;
    }
}
