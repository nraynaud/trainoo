package com.nraynaud.sport.web.converter;

import com.nraynaud.sport.web.SoftThreadLocal;
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
    public static final SoftThreadLocal<DateFormat> FORMAT_WITH_YEAR = new SoftThreadLocal<DateFormat>() {
        protected DateFormat createValue() {
            return new SimpleDateFormat("dd/MM/yy");
        }
    };

    public static final SoftThreadLocal<DateFormat> FORMAT_WITH_MONTH = new SoftThreadLocal<DateFormat>() {
        protected DateFormat createValue() {
            return new SimpleDateFormat("dd/MM");
        }
    };

    public static final SoftThreadLocal<DateFormat> FORMAT_WITH_DAY = new SoftThreadLocal<DateFormat>() {
        protected DateFormat createValue() {
            return new SimpleDateFormat("dd");
        }
    };

    @SuppressWarnings({"RawUseOfParameterizedType"})
    public Object convertFromString(final Map context, final String[] values, final Class toClass) {
        if (values != null && values.length > 0 && values[0] != null && values[0].length() > 0) {
            final String source = values[0];
            return parseDate(source);
        }
        return null;
    }

    public static Date parseDate(final String source) {
        final DateFormat sdf = FORMAT_WITH_YEAR.get();
        try {
            return sdf.parse(source);
        }
        catch (ParseException e) {
            try {
                final Date date = FORMAT_WITH_MONTH.get().parse(source);
                return setCurrentComponents(date, Calendar.YEAR);
            } catch (ParseException e1) {
                try {
                    final Date date = FORMAT_WITH_DAY.get().parse(source);
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
            final DateFormat sdf = FORMAT_WITH_YEAR.get();
            return sdf.format((Date) o);
        }
        return "";
    }
}
