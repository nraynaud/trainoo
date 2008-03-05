package com.nraynaud.sport.web;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

public class ActionDetailTest {

    @Test
    public void testCreation() {
        checkAction("namepsace|action?", "namepsace", "action", parameters());
        checkAction("namepsace|action?p1=val1", "namepsace", "action", parameters("p1", "val1"));
        checkAction("namepsace|action?p1=val1&p2=val2", "namepsace", "action", parameters("p1", "val1", "p2", "val2"));
    }

    private static HashMap<String, String[]> parameters(final String... params) {
        final HashMap<String, String[]> map = new HashMap<String, String[]>();
        for (int i = 0; i < params.length; i += 2)
            map.put(params[i], new String[]{params[i + 1]});
        return map;
    }

    private static void checkAction(final String expected, final String namespace, final String action,
                                    final HashMap<String, String[]> parameters) {
        final ActionDetail actionDetail = new ActionDetail(namespace, action, parameters);
        Assert.assertEquals(expected, actionDetail.encodedAction);
    }
}
