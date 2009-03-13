package com.nraynaud.sport.nikeplus;

import org.xml.sax.InputSource;

import java.io.File;

public class NikeWorkoutCache {
    private static final File CACHE_DIRECTORY;
    private static final String NIKE_PAGE = "http://nikeplus.nike.com/nikeplus/v1/services/app/get_run.jsp";

    static {
        final File file = new File(System.getProperty("java.io.tmpdir"), "nikeworkouts");
        if (!file.exists())
            file.mkdirs();
        CACHE_DIRECTORY = file;
    }

    private NikeWorkoutCache() {
    }

    public static InputSource getWorkoutData(final String userId, final String workoutId) {
        final String filename = userId + "_" + workoutId + ".xml";
        final File file = new File(CACHE_DIRECTORY, filename);
        final String url = NIKE_PAGE + "?id=" + workoutId + "&userID=" + userId;
        return Util.getCachedSource(url, file);
    }
}
