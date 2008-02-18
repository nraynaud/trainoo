package com.nraynaud.sport.web.converter;

import com.nraynaud.sport.MessageKind;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

public class EnumTypeConverterTest {
    @Test
    public void testObjectConvertion() {
        Assert.assertEquals(MessageKind.PRIVATE,
                new EnumTypeConverter().convertValue(Collections.emptyMap(), MessageKind.PRIVATE, Object.class));
    }
}
