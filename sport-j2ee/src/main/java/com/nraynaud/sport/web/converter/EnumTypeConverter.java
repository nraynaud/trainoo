package com.nraynaud.sport.web.converter;

import ognl.DefaultTypeConverter;

import java.util.Map;

public class EnumTypeConverter extends DefaultTypeConverter {
    @SuppressWarnings({"unchecked", "RawUseOfParameterizedType"})
    public Object convertValue(final Map context, final Object o, final Class toClass) {
        if (o instanceof String[]) {
            return convertFromString(((String[]) o)[0], toClass);
        } else if (o instanceof String) {
            return convertFromString((String) o, toClass);
        }
        if (toClass.isAssignableFrom(o.getClass()))
            return o;
        else
            return super.convertValue(context, o, toClass);
    }

    /**
     * Converts one or more String values to the specified class.
     *
     * @param value   - the String values to be converted, such as those submitted from an HTML form
     * @param toClass - the class to convert to
     * @return the converted object
     */
    public static <T extends java.lang.Enum<T>> java.lang.Enum<T> convertFromString(final String value, final Class<T> toClass) {
        return Enum.valueOf(toClass, value);
    }
}
