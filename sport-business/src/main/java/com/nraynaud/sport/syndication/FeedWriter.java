package com.nraynaud.sport.syndication;

import com.nraynaud.sport.data.GlobalWorkoutsPageData;

import java.io.IOException;
import java.io.Writer;

public interface FeedWriter {
    public void writeOn(GlobalWorkoutsPageData data, Writer writer, String format) throws IOException;
}
