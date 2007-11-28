package com.nraynaud.sport.web.converter;

import com.nraynaud.sport.web.SoftThreadLocal;
import com.opensymphony.xwork2.util.TypeConversionException;
import org.apache.struts2.util.StrutsTypeConverter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;
import java.util.Map;

@SuppressWarnings({"RawUseOfParameterizedType"})
public class DistanceConverter extends StrutsTypeConverter {
    public static final SoftThreadLocal<DecimalFormat> FORMAT = new SoftThreadLocal<DecimalFormat>() {
        protected DecimalFormat createValue() {
            return (DecimalFormat) NumberFormat.getInstance(Locale.FRANCE);
        }
    };

    public static final SoftThreadLocal<DecimalFormat> FORMAT_WITH_DOT = new SoftThreadLocal<DecimalFormat>() {
        protected DecimalFormat createValue() {
            final DecimalFormat format = (DecimalFormat) NumberFormat.getInstance(Locale.FRANCE);
            final DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
            symbols.setDecimalSeparator('.');
            format.setDecimalFormatSymbols(symbols);
            return format;
        }
    };

    public Object convertFromString(final Map context, final String[] values, final Class toClass) {
        final String input = values[0];
        final ParsePosition parsePosition = new ParsePosition(0);
        final Number number = FORMAT.get().parse(input, parsePosition);
        if (parsePosition.getIndex() != input.length())
            return parseWithDot(input);
        return number;

    }

    private static Object parseWithDot(final String input) {
        final ParsePosition parsePosition = new ParsePosition(0);
        final Number result = FORMAT_WITH_DOT.get().parse(input, parsePosition);
        if (parsePosition.getIndex() != input.length()) {
            throw new TypeConversionException("Unparseable number: " + input);
        }
        return result;
    }

    public String convertToString(final Map context, final Object o) {
        if (o == null)
            return "";
        return String.valueOf(o);
    }
}
