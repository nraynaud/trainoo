package com.nraynaud.sport.nikeplus;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;

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

    public static byte[] getWorkoutData(final String userId, final String workoutId) {
        final String filename = userId + "_" + workoutId + ".xml";
        final File file = new File(CACHE_DIRECTORY, filename);
        final ByteArrayOutputStream data = new ByteArrayOutputStream();
        try {
            return fromCache(file, data);
        } catch (FileNotFoundException e) {
            try {
                final String url = NIKE_PAGE + "?id=" + workoutId + "&userID=" + userId;
                System.out.println("getting nike workout from web: " + url);
                return fromWeb(file, data, url);
            } catch (IOException e1) {
                throw new RuntimeException(e1);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] fromWeb(final File file, final ByteArrayOutputStream data, final String url) throws
            IOException {
        final InputStream inputStream = new URL(url).openConnection().getInputStream();
        try {
            return copyToFile(file, data, inputStream);
        } finally {
            inputStream.close();
        }
    }

    private static byte[] copyToFile(final File file, final ByteArrayOutputStream data,
                                     final InputStream inputStream) throws IOException {
        IOUtils.copy(inputStream, data);
        final byte[] bytes = data.toByteArray();
        final FileOutputStream output = new FileOutputStream(file);
        try {
            IOUtils.copy(new ByteArrayInputStream(bytes), output);
        } finally {
            output.close();
        }
        return bytes;
    }

    private static byte[] fromCache(final File file, final ByteArrayOutputStream data) throws IOException {
        final FileInputStream inputStream = new FileInputStream(file);
        System.out.println("getting nike workout from cache: " + file.getAbsolutePath());
        try {
            IOUtils.copy(inputStream, data);
            return data.toByteArray();
        } finally {
            inputStream.close();
        }
    }
}
