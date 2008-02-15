package com.nraynaud.sport;

public class Helper {
    public static final String HEX_CHARS = "0123456789ABCDEF";

    private Helper() {
    }

    public static String escaped(final String string) {
        final String s = string != null && !"".equals(string) ? string : "";
        final StringBuilder str = new StringBuilder();
        escape(s, str);
        return str.toString();
    }

    public static void escape(final String input, final StringBuilder collector) {
        for (int j = 0; j < input.length(); j++) {
            final char c = input.charAt(j);

            // encode standard ASCII characters into HTML entities where needed
            if (c < '\200') {
                switch (c) {
                    case '"':
                        collector.append("&quot;");
                        break;
                    case '&':
                        collector.append("&amp;");
                        break;
                    case '<':
                        collector.append("&lt;");
                        break;
                    case '>':
                        collector.append("&gt;");
                        break;
                    default:
                        collector.append(c);
                }
            }
            // encode 'ugly' characters (ie Word "curvy" quotes etc)
            else if (c < '\377') {
                final int a = c % 16;
                final int b = (c - a) / 16;
                final String hex = "" + HEX_CHARS.charAt(b) + HEX_CHARS.charAt(a);
                collector.append("&#x").append(hex).append(";");
            }
            //add other characters back in - to handle charactersets
            //other than ascii
            else {
                collector.append(c);
            }
        }
    }

    public static String nonEscaped(final UserString string) {
        return string == null ? null : string.nonEscaped();
    }
}
