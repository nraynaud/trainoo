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

import javax.xml.namespace.QName;
import javax.xml.xpath.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class NikePlusExtractor implements Importer {
    private static final XPathExpression STATUS_EXPRESSION;
    private static final XPathExpression RUN_LIST;
    private final static XPathExpression DISTANCE;
    private final static XPathExpression DURATION;
    private final static XPathExpression ENERGY;
    private final static XPathExpression START_TIME;
    private final static XPathExpression USER_ID;

    static {
        try {
            final XPath xPath = XPathFactory.newInstance().newXPath();
            STATUS_EXPRESSION = xPath.compile("/*/status");
            RUN_LIST = xPath.compile("/plusService/runList/*");
            DISTANCE = xPath.compile("distance");
            DURATION = xPath.compile("duration");
            ENERGY = xPath.compile("calories");
            START_TIME = xPath.compile("startTime");
            USER_ID = xPath.compile("/plusService/user/@id");
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    public static void checkStatus(final byte[] body) throws FailureException {
        try {
            final String status = evaluate(body, STATUS_EXPRESSION, XPathConstants.STRING);
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
        final HttpClient client = connectedClient(login, password);
        final byte[] responseBody = get(client, "http://nikeplus.nike.com/nikeplus/v1/services/app/run_list.jsp");
        extractWorkouts(responseBody, workoutCollector);
    }

    public static String getNikePlusId(final String login, final String password) throws
            IOException,
            FailureException {
        final HttpClient client = connectedClient(login, password);
        final byte[] responseBody = get(client,
                "http://secure-nikeplus.nike.com/nikeplus/v1/services/app/get_user_data.jhtml");
        return extractUserId(responseBody);
    }

    public static String extractUserId(final byte[] responseBody) {
        try {
            return evaluate(responseBody, USER_ID, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    private static HttpClient connectedClient(final String login, final String password) throws
            IOException,
            FailureException {
        final HttpClient client = new HttpClient();
        connect(client, login, password);
        return client;
    }

    private static byte[] get(final HttpClient client, final String uri) throws IOException {
        final GetMethod getMethod = new GetMethod(uri);
        try {
            client.executeMethod(getMethod);
            return getMethod.getResponseBody();
        } finally {
            getMethod.releaseConnection();
        }
    }

    public static void extractWorkouts(final byte[] responseBody, final WorkoutCollector workoutCollector) throws
            FailureException,
            ParseException {
        checkStatus(responseBody);
        try {
            final NodeList result = evaluate(responseBody, RUN_LIST, XPathConstants.NODESET);
            for (int i = 0; i < result.getLength(); i++) {
                final Node node = result.item(i);
                final String nikeId = node.getAttributes().getNamedItem("id").getTextContent();
                final String distance = DISTANCE.evaluate(node);
                final String duration = DURATION.evaluate(node);
                final String energy = ENERGY.evaluate(node);
                final String date = START_TIME.evaluate(node);
                workoutCollector.collectWorkout(nikeId, "course", new SimpleDateFormat("yyyy-MM-dd").parse(date),
                        Double.valueOf(distance), Long.parseLong(duration) / 1000, Double.valueOf(energy).longValue());
            }
            workoutCollector.endCollection();
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({"unchecked"})
    private static <T> T evaluate(final byte[] responseBody, final XPathExpression runList,
                                  final QName returnType) throws XPathExpressionException {
        return (T) runList.evaluate(new InputSource(new ByteArrayInputStream(responseBody)), returnType);
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
        } catch (ParseException e) {
            throw new FailureException(e);
        }
    }

    public String getId(final String login, final String password) throws FailureException {
        try {
            return getNikePlusId(login, password);
        } catch (IOException e) {
            throw new FailureException(e);
        }
    }
}
