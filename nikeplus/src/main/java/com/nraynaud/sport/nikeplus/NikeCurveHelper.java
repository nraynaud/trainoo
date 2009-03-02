package com.nraynaud.sport.nikeplus;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.util.*;

public class NikeCurveHelper {

    private static final int MAX_FINAL_POINTS = 20;

    private NikeCurveHelper() {
    }

    private static class Point implements Comparable<Point> {
        public final double distance;
        public double pace;

        public Point(final double distance, final double pace) {
            this.distance = distance;
            this.pace = pace;
        }

        public String toString() {
            return "[" + distance + ", " + pace + ']';
        }

        public int compareTo(final Point o) {
            return Double.compare(distance, o.distance);
        }

        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Point point = (Point) o;
            return Double.compare(point.distance, distance) == 0;
        }

        public int hashCode() {
            final long temp = distance != +0.0d ? Double.doubleToLongBits(distance) : 0L;
            return (int) (temp ^ temp >>> 32);
        }
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

    public static String getLowPassCurve(final String userId, final String workoutId, final int radius) {
        final XPath xPath = XPathFactory.newInstance().newXPath();
        try {
            final Node root = (Node) xPath.evaluate("/",
                    new InputSource(new ByteArrayInputStream(NikeWorkoutCache.getWorkoutData(userId, workoutId))),
                    XPathConstants.NODE);
            final SortedSet<Point> points = new TreeSet<Point>();
            final Node extended = (Node) xPath.evaluate("//extendedData[@dataType='distance']", root,
                    XPathConstants.NODE);
            registerExtandedLowPass(xPath, points, extended);
            runningAverageLowPassFilter(radius, points);
            for (final Point point : points) {
                point.pace = -point.pace / 60000; //go to minutes/km
            }
            return points.toString();
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    private static void runningAverageLowPassFilter(final int radius, final Collection<Point> points) {
        final List<Point> pristine = new ArrayList<Point>(points);
        int index = 0;
        for (final Point point : points) {
            int count = 0;
            double accumulator = 0.0;
            for (int j = index - radius; j <= index + radius; j++)
                try {
                    accumulator += pristine.get(j).pace;
                    count++;
                } catch (IndexOutOfBoundsException e) {
                    //ok, no big deal, just too close from the bounds
                }
            point.pace = accumulator / count;
            index++;
        }
    }

    private static void registerExtandedLowPass(final XPath xPath, final Set<Point> points,
                                                final Node extendedDataNode) throws
            XPathExpressionException {
        final double sampling = Double.parseDouble(xPath.evaluate("@intervalValue", extendedDataNode));
        final String extendedData = xPath.evaluate("text()", extendedDataNode);
        double previousDistance = 0.0;
        final double samplingMilisec = sampling * 1000;
        for (final String fragment : ("0," + extendedData).split(",")) {
            final double distance = Double.parseDouble(fragment);
            final double pace = samplingMilisec / (distance - previousDistance);
            if (!Double.isInfinite(pace) && !Double.isNaN(pace)) {
                points.add(new Point(distance, pace));
            }
            previousDistance = distance;
        }
    }

    public static String getNikePlusCurve(final String userId, final String workoutId) {
        final XPath xPath = XPathFactory.newInstance().newXPath();
        try {
            final Node root = (Node) xPath.evaluate("/",
                    new InputSource(new ByteArrayInputStream(NikeWorkoutCache.getWorkoutData(userId, workoutId))),
                    XPathConstants.NODE);
            final double totalDistance = Double.parseDouble(
                    (String) xPath.evaluate("//runSummary/distance/text()", root, XPathConstants.STRING));
            final SortedSet<Point> points = new TreeSet<Point>();
            final Node extended = (Node) xPath.evaluate("//extendedData[@dataType='distance']", root,
                    XPathConstants.NODE);
            final Config config = new Config(totalDistance / MAX_FINAL_POINTS);
            registerExtanded(xPath, points, extended, config);
            final TreeSet<Point> snaps = new TreeSet<Point>();
            //order is important !
            addSnapshot(xPath, root, points, snaps, "//snapShotList[@snapShotType='kmSplit']/snapShot", config);
            addSnapshot(xPath, root, points, snaps, "//snapShotList[@snapShotType='userClick']/snapShot", config);
            points.addAll(snaps);
            points.last().pace = points.first().pace;
            dumpPoints(points, config);
            for (final Point point : points) {
                point.pace = -point.pace / 60000; //go to minutes/km
            }
            return points.toString();
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    private static void registerExtanded(final XPath xPath, final Set<Point> points,
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
            if (i != 0
                    && i % (distances.size() / MAX_FINAL_POINTS) == 0
                    && !Double.isInfinite(pace)
                    && !Double.isNaN(
                    pace)) {
                config.updateMinMax(pace);
                points.add(new Point(distance, pace));
                log.append(" (selected)");
                if (first) {
                    points.add(new Point(0, pace));
                    first = false;
                }
            }
            logText(log.toString());
            previousDistance = distance;
            previousTime = time;
        }
    }

    private static void dumpPoints(final Set<Point> points, final Config config) {
        logText("points: " + points.size());
        logText("min: " + config.minPace + "\tmax: " + config.maxPace);
        int i = 0;
        for (final Point point : points) {
            logText("point "
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

    private static void logText(final String text) {
        //noinspection ConstantIfStatement
        if (false) {
            System.out.println(text);
        }
    }

    private static void addSnapshot(final XPath xPath, final Object root, final Set<Point> points,
                                    final TreeSet<Point> snaps, final String query,
                                    final Config config) throws
            XPathExpressionException {
        final NodeList snapshots = (NodeList) xPath.evaluate(query, root,
                XPathConstants.NODESET);
        for (int i = 0; i < snapshots.getLength(); i++) {
            final Node snapShotNode = snapshots.item(i);
            final double pace = Double.parseDouble(xPath.evaluate("pace/text()", snapShotNode));
            final double distance = Double.parseDouble(xPath.evaluate("distance/text()", snapShotNode));
            final String event = xPath.evaluate("@event", snapShotNode);
            final Point snap = new Point(distance, pace);
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
                    logText("(addind snap)");
                } else
                    logText("(removing snap)");
            } else
                logText("(ignoring snap no dist)");
        }
    }

    //to optimise, the tree is ordered
    private static boolean isFarEnough(final TreeSet<Point> snaps, final Point snap,
                                       final Config config) {
        for (final Point point : snaps) {
            if (isclose(config, point, snap))
                return false;
        }
        return true;
    }

    //to optimise, the tree is ordered
    private static void removeClosePoints(final Set<Point> points, final Point snap,
                                          final Config config) {
        for (Iterator<Point> it = points.iterator(); it.hasNext();) {
            final Point point1 = it.next();
            if (isclose(config, point1, snap)) {
                it.remove();
                twistPace(snap, point1, config);
            }
        }
    }

    private static void twistPace(final Point snap, final Point point, final Config config) {
        if (point.pace == config.maxPace || point.pace == config.minPace)
            snap.pace = point.pace;
    }

    private static boolean isclose(final Config config, final Point point1, final Point point) {
        return Math.abs(point.distance - point1.distance) < config.distanceRange;
    }
}
