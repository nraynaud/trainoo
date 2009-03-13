package com.nraynaud.sport.nikeplus;

import org.apache.commons.io.IOUtils;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.net.URL;

public class Util {
    static final XPath XPATH = XPathFactory.newInstance().newXPath();

    private Util() {
    }

    static XPathExpression compile(final String expression) {
        try {
            return XPATH.compile(expression);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    public static InputSource getCachedSource(final String url, final File file) {
        return new InputSource(new ByteArrayInputStream(getCached(url, file)));
    }

    public static byte[] getCached(final String url, final File cacheFile) {
        final ByteArrayOutputStream data = new ByteArrayOutputStream();
        try {
            return fromCache(cacheFile, data);
        } catch (FileNotFoundException e) {
            try {
                System.out.println("getting from web: " + url);
                return fromWeb(cacheFile, data, url);
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
        System.out.println("getting from cache: " + file.getAbsolutePath());
        try {
            IOUtils.copy(inputStream, data);
            return data.toByteArray();
        } finally {
            inputStream.close();
        }
    }
}
