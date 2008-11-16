package com.nraynaud.sport.formatting;

import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;

public class ConverterUtil {
    private ConverterUtil() {
    }

    public static Object parseWholeString(final Format format, final String input) throws ParseException {
        final ParsePosition position = new ParsePosition(0);
        final Object result = format.parseObject(input, position);
        if (position.getIndex() != input.length())
            throw new ParseException(input, position.getIndex());
        return result;
    }
}
