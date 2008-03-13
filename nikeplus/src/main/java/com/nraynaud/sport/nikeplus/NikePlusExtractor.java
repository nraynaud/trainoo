package com.nraynaud.sport.nikeplus;

import com.nraynaud.sport.importer.FailureException;
import com.nraynaud.sport.importer.Importer;
import com.nraynaud.sport.importer.WorkoutCollector;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.xpath.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class NikePlusExtractor implements Importer {
    public static final XPathExpression STATUS_EXPRESSION;

    static {
        try {
            STATUS_EXPRESSION = XPathFactory.newInstance().newXPath().compile("/*/status");
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    public static void checkStatus(final byte[] body) throws FailureException {
        try {
            final String status = STATUS_EXPRESSION.evaluate(new InputSource(new ByteArrayInputStream(body)));
            if (!"success".equals(status))
                throw new FailureException();
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    public static void collectNikePlusWorkouts(final String login, final String password,
                                               final WorkoutCollector workoutCollector) throws
            IOException,
            FailureException,
            ParseException {
        final HttpClient client = new HttpClient();
        connect(client, login, password);
        final GetMethod getMethod = new GetMethod("http://nikeplus.nike.com/nikeplus/v1/services/app/run_list.jsp");
        final byte[] responseBody;
        try {
            client.executeMethod(getMethod);
            responseBody = getMethod.getResponseBody();
        } finally {
            getMethod.releaseConnection();
        }
        extractWorkouts(responseBody, workoutCollector);
        System.out.println(getMethod.getResponseBodyAsString());
    }

    public static void extractWorkouts(final byte[] responseBody, final WorkoutCollector workoutCollector) throws
            FailureException,
            ParseException {
        checkStatus(responseBody);
        final XPath xpa = XPathFactory.newInstance().newXPath();
        try {
            final NodeList result = (NodeList) xpa.evaluate("/plusService/runList/*",
                    new InputSource(new ByteArrayInputStream(responseBody)), XPathConstants.NODESET);
            for (int i = 0; i < result.getLength(); i++) {
                final Node node = result.item(i);
                final String nikeId = node.getAttributes().getNamedItem("id").getTextContent();
                final String distance = (String) xpa.evaluate("distance", node, XPathConstants.STRING);
                final String duration = (String) xpa.evaluate("duration", node, XPathConstants.STRING);
                final String date = (String) xpa.evaluate("startTime", node, XPathConstants.STRING);
                workoutCollector.collectWorkout(nikeId, "course", new SimpleDateFormat("yyyy-MM-dd").parse(date),
                        Double.valueOf(distance), Long.parseLong(duration) / 1000);
            }
            workoutCollector.endCollection();
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    public static void connect(final HttpClient client, final String login, final String password) throws
            IOException,
            FailureException {
        final PostMethod method = new PostMethod("https://secure-nikeplus.nike.com/services/profileService");
        method.addParameter("login", login);
        method.addParameter("password", password);
        method.addParameter("action", "login");
        method.addParameter("locale", "fr_FR");
        try {
            client.executeMethod(method);
            checkStatus(method.getResponseBody());
        } finally {
            method.releaseConnection();
        }
    }

    public void importWorkouts(final String login, final String password, final WorkoutCollector collector) throws
            FailureException {
        try {
            collectNikePlusWorkouts(login, password, collector);
        } catch (IOException e) {
            throw new FailureException(e);
        } catch (FailureException e) {
            throw new FailureException(e);
        } catch (ParseException e) {
            throw new FailureException(e);
        }
    }
}
