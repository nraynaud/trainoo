package com.nraynaud.sport.web.action.track;

import org.junit.Assert;
import org.junit.Test;

public class CreateActionTest {
    @Test
    public void testParsing() {
        Assert.assertTrue(EditAction.matchPoint("[2.3,3.0]"));
        Assert.assertTrue(EditAction.matchPoint("[2.3,3.0],[2.3,3.0]"));
        Assert.assertTrue(EditAction.matchPoint("[-2.3,3.0],[2.3,3.0]"));
        Assert.assertFalse(EditAction.matchPoint("[2.3,3.0],[2.3,3"));
        Assert.assertFalse(EditAction.matchPoint("[2.3,3.0],[2.3,3"));
        Assert.assertFalse(EditAction.matchPoint("[a2.3,3.0]"));
        Assert.assertFalse(EditAction.matchPoint("[2.3,3.0],[2.3,3.0],[2sdf]"));
    }
}
