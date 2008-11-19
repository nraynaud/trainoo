package com.nraynaud.sport;

import org.junit.Assert;
import org.junit.Test;

public class HelperTest {
    private static final String[][] TESTS = {
            {"a<b", "a&lt;b", "a\\x3cb"},
            {"a>b", "a&gt;b", "a\\x3eb"},
            {"a&b", "a&amp;b", "a\\x26b"},
            {"a\"b", "a&quot;b", "a\\x22b"},
            {"a\'b", "a&#39;b", "a\\x27b"},
            {"a\201b", "a&#x81;b", "a\201b"},
    };

    @Test
    public void testEscape() {
        for (final String[] test : TESTS) {
            Assert.assertEquals(test[1], Helper.escaped(test[0]));
            Assert.assertEquals(test[2], Helper.escapedForJavascript(test[0]));
        }
    }
}
