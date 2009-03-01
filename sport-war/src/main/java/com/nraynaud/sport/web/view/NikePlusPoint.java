package com.nraynaud.sport.web.view;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class NikePlusPoint implements Comparable<NikePlusPoint> {
    public final double distance;
    public double pace;
    private static final int MAX_FINAL_POINTS = 20;

    public NikePlusPoint(final double distance, final double pace) {
        this.distance = distance;
        this.pace = pace;
    }

    public String toString() {
        return "[" + distance + ", " + pace + ']';
    }

    public int compareTo(final NikePlusPoint o) {
        return Double.compare(distance, o.distance);
    }

    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final NikePlusPoint point = (NikePlusPoint) o;
        return Double.compare(point.distance, distance) == 0;
    }

    public int hashCode() {
        final long temp = distance != +0.0d ? Double.doubleToLongBits(distance) : 0L;
        return (int) (temp ^ temp >>> 32);
    }

    private static class Config {
        public final double distanceRange;
        public double minPace = Double.MAX_VALUE;
        public double maxPace;

        public Config(final double distanceRange) {
            this.distanceRange = distanceRange;
        }

        public void updateMinMax(final double pace) {
            minPace = Math.min(pace, minPace);
            maxPace = Math.max(pace, maxPace);
        }
    }

    public static String getNikePlusData(final String userId, final String workoutId) {
        final XPath xPath = XPathFactory.newInstance().newXPath();
        try {
            final String url = "http://nikeplus.nike.com/nikeplus/v1/services/app/get_run.jsp?id="
                    + workoutId
                    + "&userID="
                    + userId;
            final Node root = (Node) xPath.evaluate("/", new InputSource(new URL(url).openStream()),
                    XPathConstants.NODE);
            final double totalDistance = Double.parseDouble(
                    (String) xPath.evaluate("//runSummary/distance/text()", root, XPathConstants.STRING));
            final SortedSet<NikePlusPoint> points = new TreeSet<NikePlusPoint>();
            final Node extended = (Node) xPath.evaluate("//extendedData[@dataType='distance']", root,
                    XPathConstants.NODE);
            final Config config = new Config(totalDistance / MAX_FINAL_POINTS);
            registerExtanded(xPath, points, extended, config);
            final TreeSet<NikePlusPoint> snaps = new TreeSet<NikePlusPoint>();
            //order is important !
            addSnapshot(xPath, root, points, snaps, "//snapShotList[@snapShotType='kmSplit']/snapShot", config);
            addSnapshot(xPath, root, points, snaps, "//snapShotList[@snapShotType='userClick']/snapShot", config);
            points.addAll(snaps);
            points.last().pace = points.first().pace;
            dumpPoints(points, config);
            for (final NikePlusPoint point : points) {
                point.pace = -point.pace / 60000; //go to minutes/km
            }
            return points.toString();
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void registerExtanded(final XPath xPath, final Set<NikePlusPoint> points,
                                         final Node extendedDataNode,
                                         final Config config) throws XPathExpressionException {
        final double sampling = Double.parseDouble(xPath.evaluate("@intervalValue", extendedDataNode));
        final String extendedData = xPath.evaluate("text()", extendedDataNode);
        double previousTime = 0.0;
        double previousDistance = 0.0;
        final List<String> distances = new ArrayList<String>(Arrays.asList(extendedData.split(",")));
        if (Double.parseDouble(distances.get(0)) != 0)
            distances.add(0, "0");
        boolean first = true;
        for (int i = 0; i < distances.size(); i++) {
            final StringBuilder log = new StringBuilder();
            final int time = (int) (i * sampling * 1000);
            final String fragment = distances.get(i);
            final double distance = Double.parseDouble(fragment);
            final double pace = (time - previousTime) / (distance - previousDistance);
            log.append("testing point ")
                    .append(i)
                    .append(":\tdist: ")
                    .append(distance)
                    .append("\tdelta: ")
                    .append(distance - previousDistance)
                    .append("\tpace: ")
                    .append(pace);
            if (i != 0 && i % (distances.size() / MAX_FINAL_POINTS) == 0 && !Double.isInfinite(pace) && !Double.isNaN(
                    pace)) {
                config.updateMinMax(pace);
                points.add(new NikePlusPoint(distance, pace));
                log.append(" (selected)");
                if (first) {
                    points.add(new NikePlusPoint(0, pace));
                    first = false;
                }
            }
            System.out.println(log);
            previousDistance = distance;
            previousTime = time;
        }
    }

    private static void dumpPoints(final Set<NikePlusPoint> points, final Config config) {
        System.out.println("points: " + points.size());
        System.out.println("min: " + config.minPace + "\tmax: " + config.maxPace);
        int i = 0;
        for (final NikePlusPoint point : points) {
            System.out
                    .println("point "
                            + i
                            + "\t"
                            + "pace: "
                            + (int) point.pace
                            + " ms/km\tdist: "
                            + point
                            .distance
                            + " km");
            i++;
        }
    }

    private static void addSnapshot(final XPath xPath, final Object root, final Set<NikePlusPoint> points,
                                    final TreeSet<NikePlusPoint> snaps, final String query, final Config config) throws
            XPathExpressionException {
        final NodeList snapshots = (NodeList) xPath.evaluate(query, root,
                XPathConstants.NODESET);
        for (int i = 0; i < snapshots.getLength(); i++) {
            final Node snapShotNode = snapshots.item(i);
            final double pace = Double.parseDouble(xPath.evaluate("pace/text()", snapShotNode));
            final double distance = Double.parseDouble(xPath.evaluate("distance/text()", snapShotNode));
            final String event = xPath.evaluate("@event", snapShotNode);
            final NikePlusPoint snap = new NikePlusPoint(distance, pace);
            if (event.equals("")
                    || event.equals("stop")
                    || event.equals("powerSong")
                    || event.equals("onDemandVP")
                    || event.equals("activationPoint")) {
                if (event.equals("")
                        || event.equals("stop")
                        || event.equals("powerSong")
                        || isFarEnough(snaps, snap, config)) {
                    removeClosePoints(points, snap, config);
                    snaps.add(snap);
                    System.out.println("(addind snap)");
                } else
                    System.out.println("(removing snap)");
            } else
                System.out.println("(ignoring snap no dist)");
        }
    }

    //to optimise, the tree is ordered
    private static boolean isFarEnough(final TreeSet<NikePlusPoint> snaps, final NikePlusPoint snap,
                                       final Config config) {
        for (final NikePlusPoint point : snaps) {
            if (isclose(config, point, snap))
                return false;
        }
        return true;
    }

    //to optimise, the tree is ordered
    private static void removeClosePoints(final Set<NikePlusPoint> points, final NikePlusPoint snap,
                                          final Config config) {
        for (Iterator<NikePlusPoint> it = points.iterator(); it.hasNext();) {
            final NikePlusPoint point1 = it.next();
            if (isclose(config, point1, snap)) {
                it.remove();
                twistPace(snap, point1, config);
            }
        }
    }

    private static void twistPace(final NikePlusPoint snap, final NikePlusPoint point, final Config config) {
        if (point.pace == config.maxPace || point.pace == config.minPace)
            snap.pace = point.pace;
    }

    private static boolean isclose(final Config config, final NikePlusPoint point1, final NikePlusPoint point) {
        return Math.abs(point.distance - point1.distance) < config.distanceRange;
    }
}
