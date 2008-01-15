package com.nraynaud.sport.web.converter;

import com.nraynaud.sport.web.SoftThreadLocal;
import static com.nraynaud.sport.web.converter.ConverterUtil.parseWholeString;
import com.opensymphony.xwork2.util.TypeConversionException;
import org.apache.struts2.util.StrutsTypeConverter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
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
        return parseDistance(removeKmSuffix(input));
    }

    private static String removeKmSuffix(final String input) {
        final String next;
        if (input.endsWith("km"))
            next = input.substring(0, input.length() - 2);
        else
            next = input;
        return next;
    }

    public String convertToString(final Map context, final Object o) {
        if (o == null)
            return "";
        return formatNumber((Double) o);
    }

    public static Double parseDistance(final String input) {
        return Double.valueOf(ParseNumber(input).doubleValue());
    }

    private static Number ParseNumber(final String input) {
        try {
            return (Number) parseWholeString(FORMAT.get(), input);
        } catch (ParseException e) {
            try {
                return (Number) parseWholeString(FORMAT_WITH_DOT.get(), input);
            } catch (ParseException e1) {
                throw new TypeConversionException("Unparseable number: " + input);
            }
        }
    }

    public static String formatNumber(final Double o) {
        return FORMAT.get().format(o);
    }
}
