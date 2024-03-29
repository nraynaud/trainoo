package com.nraynaud.sport.web;

import com.nraynaud.sport.UserStringImpl;
import static com.nraynaud.sport.web.view.Helpers.multilineText;
import static com.nraynaud.sport.web.view.Helpers.pluralize;
import org.junit.Assert;
import org.junit.Test;

public class HelpersTest {
    @Test
    public void testMultilineText() {
        checkMultiline("lol", "lol");
        checkMultiline("lol&lt;", "lol<");
        checkMultiline("lol<br>lol2", "lol\nlol2");
        checkMultiline("lol&lt;", "lol<\n");
        checkMultiline("lol&lt;<br>lol2&amp;", "lol<\nlol2&");
        checkMultiline("<a href='http://lol.com/'>http://lol.com/</a>", "http://lol.com/");
        checkMultiline("allez sur <a href='http://lol.com/'>http://lol.com/</a> &lt;<br>lol",
                "allez sur http://lol.com/ <\nlol");
    }

    private static void checkMultiline(final String expected, final String input) {
        final String output = multilineText(UserStringImpl.valueOf(input));
        Assert.assertEquals(expected, output);
    }

    @Test
    public void testPluralize() {
        Assert.assertEquals("0", pluralize(-1, "0", "", ""));
        Assert.assertEquals("0", pluralize(0, "0", "", ""));
        Assert.assertEquals("1", pluralize(1, "", "1", ""));
        Assert.assertEquals("2", pluralize(2, "", "", "2"));
        Assert.assertEquals("1", pluralize(1, "1", ""));
        Assert.assertEquals("2", pluralize(2, "", "2"));
    }
}
