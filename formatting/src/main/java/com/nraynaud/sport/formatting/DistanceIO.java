package com.nraynaud.sport.formatting;

import static com.nraynaud.sport.formatting.ConverterUtil.parseWholeString;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class DistanceIO {
    private DistanceIO() {
    }

    public static Double parseDistance(final String input) throws ParseException {
        final double result = parseNumber(removeKmSuffix(input)).doubleValue();
        if (result < 0.1)
            throw new ParseException(input, 0);
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

    private static Number parseNumber(final String input) throws ParseException {
        try {
            return (Number) parseWholeString(getFormat(), input);
        } catch (ParseException e) {
            return (Number) parseWholeString(getFormatWithDot(), input);
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
