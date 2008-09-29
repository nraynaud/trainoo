package com.nraynaud.sport;

import java.util.Random;

public class Helper {
    public static final String HEX_CHARS = "0123456789ABCDEF";
    private static final Random RANDOM = new Random();

    private Helper() {
    }

    public static String escaped(final String string) {
        final String s = string != null && !"".equals(string) ? string : "";
        final StringBuilder str = new StringBuilder();
        escape(s, str);
        return str.toString();
    }

    public static String escapedForJavascript(final String string) {
        final String s = string != null && !"".equals(string) ? string : "";
        final StringBuilder str = new StringBuilder();
        escapeJavascript(s, str);
        return str.toString();
    }

    private static String hexify(final char c) {
        final int a = c % 16;
        final int b = (c - a) / 16;
        return "" + HEX_CHARS.charAt(b) + HEX_CHARS.charAt(a);
    }

    public static void escapeJavascript(final String input, final StringBuilder collector) {
        for (int j = 0; j < input.length(); j++) {
            final char inChar = input.charAt(j);
            final String outString;
            outString = javascriptEscape(inChar);
            collector.append(outString);
        }
    }

    private static String javascriptEscape(final char inChar) {
        if (inChar < '\040' || "\\\\'><&=-;".indexOf(inChar) >= 0)
            return "\\x" + hexify(inChar);
        else
            return new String(new char[]{inChar});
    }

    public static void escape(final String input, final StringBuilder collector) {
        for (int j = 0; j < input.length(); j++)
            collector.append(htmlEscape(input.charAt(j)));
    }

    private static String htmlEscape(final char inChar) {
        // encode standard ASCII characters into HTML entities where needed
        switch (inChar) {
            case '\'':
                return "&#39;";
            case '"':
                return "&quot;";
            case '&':
                return "&amp;";
            case '<':
                return "&lt;";
            case '>':
                return "&gt;";
            default:
                // encode 'ugly' characters (ie Word "curvy" quotes etc)
                if (inChar >= '\200' && inChar < '\377')
                    return "&#x" + hexify(inChar) + ";";
                    //add other characters back in - to handle charactersets
                    //other than ascii
                else
                    return new String(new char[]{inChar});
        }
    }

    public static String nonEscaped(final UserString string) {
        return string == null ? null : string.nonEscaped();
    }

    public static String escaped(final UserString string) {
        return escaped(string.nonEscaped());
    }

    private static String randomstring(final int minCharacters, final int maxCharacters) {
        final int n = rand(minCharacters, maxCharacters);
        final StringBuilder characters = new StringBuilder(n);
        for (int i = 0; i < n; i++)
            characters.append((char) rand('a', 'z'));
        return characters.toString();
    }

    private static int rand(final int min, final int max) {
        final int range = max - min + 1;
        final int i = Math.abs(RANDOM.nextInt() % range);
        return min + i;
    }

    public static String randomstring() {
        return randomstring(6, 9);
    }
}
