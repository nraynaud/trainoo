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
        for (int j=0; j < input.length(); j++) {
            final char c = input.charAt(j);
            if (c < '\040') {
                collector.append("\\x").append(hexify(c));
            } else switch (c) {
                case '\\':
                    collector.append("\\x5C");
                    break;
                case '\'':
                    collector.append("\\x27");
                    break;
                case '"':
                    collector.append("\\x22");
                    break;
                case '>':
                    collector.append("\\x3E");
                    break;
                case '<':
                    collector.append("\\x3C");
                    break;
                case '&':
                    collector.append("\\x26");
                    break;
                case '=':
                    collector.append("\\x3D");
                    break;
                case '-':
                    collector.append("\\x2D");
                    break;
                case ';':
                    collector.append("\\x3B");
                    break;
                default:
                    collector.append(c);
            }
        }
    }

    public static void escape(final String input, final StringBuilder collector) {
        for (int j = 0; j < input.length(); j++) {
            final char c = input.charAt(j);

            // encode standard ASCII characters into HTML entities where needed
            if (c < '\200') {
                switch (c) {
                    case '\'':
                        collector.append("&#39;");
                        break;
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
                collector.append("&#x").append(hexify(c)).append(";");
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
