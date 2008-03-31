package com.nraynaud.sport.web.converter;

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

    public Object convertFromString(final Map context, final String[] values, final Class toClass) {
        final String input = values[0];
        return parseDistance(input);
    }

    public String convertToString(final Map context, final Object o) {
        if (o == null)
            return "";
        return formatDistance((Double) o);
    }

    public static Double parseDistance(final String input) {
        final double result = parseNumber(removeKmSuffix(input)).doubleValue();
        if (result < 0.1)
            throw new TypeConversionException("Negative or null number");
        return Double.valueOf(result);
    }

    private static String removeKmSuffix(final String input) {
        final String next;
        if (input.endsWith("km"))
            next = input.substring(0, input.length() - 2);
        else
            next = input;
        return next;
    }

    private static Number parseNumber(final String input) {
        try {
            return (Number) parseWholeString(getFormat(), input);
        } catch (ParseException e) {
            try {
                return (Number) parseWholeString(getFormatWithDot(), input);
            } catch (ParseException e1) {
                throw new TypeConversionException("Unparseable number: " + input);
            }
        }
    }

    public static String formatDistance(final Double o) {
        return getFormat().format(o);
    }

    private static DecimalFormat getFormatWithDot() {
        final DecimalFormat format = (DecimalFormat) NumberFormat.getInstance(Locale.FRANCE);
        final DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        format.setDecimalFormatSymbols(symbols);
        return format;
    }

    private static DecimalFormat getFormat() {
        final DecimalFormat format = (DecimalFormat) NumberFormat.getInstance(Locale.FRANCE);
        format.setGroupingUsed(false);
        format.setMaximumFractionDigits(2);
        return format;
    }
}
