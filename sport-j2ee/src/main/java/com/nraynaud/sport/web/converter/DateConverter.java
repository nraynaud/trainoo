package com.nraynaud.sport.web.converter;

import static com.nraynaud.sport.web.converter.ConverterUtil.parseWholeString;
import com.opensymphony.xwork2.util.TypeConversionException;
import org.apache.struts2.util.StrutsTypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

public class DateConverter extends StrutsTypeConverter {

    @SuppressWarnings({"RawUseOfParameterizedType"})
    public Object convertFromString(final Map context, final String[] values, final Class toClass) {
        final String source = values[0];
        return parseDate(source);
    }

    public static Date parseDate(final String source) {
        try {
            return (Date) parseWholeString(getFormatWithYear(), source);
        }
        catch (ParseException e) {
            try {
                final Date date = (Date) parseWholeString(getFormatWithMonth(), source);
                return setCurrentComponents(date, Calendar.YEAR);
            } catch (ParseException e1) {
                try {
                    final Date date = (Date) parseWholeString(getFormatWithDay(), source);
                    return setCurrentComponents(date, Calendar.YEAR, Calendar.MONTH);
                } catch (ParseException e2) {
                    throw new TypeConversionException(e);
                }
            }
        }
    }


    // returns a new date whose component (like Calendar.YEAR) are overriden with the current one.
    private static Date setCurrentComponents(final Date date, final int... compents) {
        final GregorianCalendar calendar = new GregorianCalendar();
        final int saved[] = new int[compents.length];
        for (int i = 0; i < compents.length; i++)
            saved[i] = calendar.get(compents[i]);
        calendar.setTime(date);
        for (int i = 0; i < saved.length; i++)
            calendar.set(compents[i], saved[i]);
        return calendar.getTime();
    }

    @SuppressWarnings({"RawUseOfParameterizedType"})
    public String convertToString(final Map context, final Object o) {
        if (o instanceof Date) {
            final DateFormat sdf = getFormatWithYear();
            return sdf.format((Date) o);
        }
        return "";
    }

    private static DateFormat getFormatWithYear() {
        return new SimpleDateFormat("dd/MM/yy");
    }

    private static DateFormat getFormatWithMonth() {
        return new SimpleDateFormat("dd/MM");
    }

    private static DateFormat getFormatWithDay() {
        return new SimpleDateFormat("dd");
    }
}
