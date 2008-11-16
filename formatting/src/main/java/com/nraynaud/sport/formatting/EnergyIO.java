package com.nraynaud.sport.formatting;

import static com.nraynaud.sport.formatting.ConverterUtil.parseWholeString;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class EnergyIO {
    private EnergyIO() {
    }

    public static Long parseEnergy(final String input) throws ParseException {
        final Long energy = (Long) parseWholeString(getFormat(), input);
        if (energy < 10)
            throw new ParseException(input, 0);
        return energy;
    }

    public static String formatEnergy(final Long energy) {
        return getFormat().format(energy);
    }

    public static DecimalFormat getFormat() {
        final DecimalFormat format = (DecimalFormat) NumberFormat.getInstance(Locale.FRANCE);
        format.setParseIntegerOnly(true);
        return format;
    }
}
