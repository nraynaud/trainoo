package com.nraynaud.sport.nikeplus;

import static com.nraynaud.sport.nikeplus.NikeWorkoutCache.getWorkoutData;
import static com.nraynaud.sport.nikeplus.Util.compile;
import com.nraynaud.sport.nikeplus.data.Workout;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import static java.lang.Double.parseDouble;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//all this dumb logging is for comparison with nike flash log outputs.
public class NikeCurveHelper {

    private static final int MAX_FINAL_POINTS = 20;

    private static final XPathExpression EXTENDED_DATA = compile("//extendedData[@dataType='distance']");
    private static final XPathExpression ROOT = compile("/");
    private static final XPathExpression INTERVAL = compile("@intervalValue");
    private static final XPathExpression TEXT = compile("text()");
    private static final XPathExpression TOTAL_DISTANCE = compile("/plusService/sportsData/runSummary/distance/text()");
    private static final XPathExpression TOTAL_DURATION = compile("/plusService/sportsData/runSummary/duration/text()");
    private static final XPathExpression TOTAL_ENERGY = compile("/plusService/sportsData/runSummary/calories/text()");
    private static final XPathExpression KM_SNAPSHOTS = compile(
            "/plusService/sportsData/snapShotList[@snapShotType='kmSplit']/snapShot");
    private static final XPathExpression CLICK_SNAPSHOTS = compile(
            "//snapShotList[@snapShotType='userClick']/snapShot");
    private static final XPathExpression PACE = compile("pace/text()");
    private static final XPathExpression DISTANCE = compile("distance/text()");
    private static final XPathExpression EVENT = compile("@event");
    private static final XPathExpression DATE = compile("/plusService/sportsData/startTime/text()");

    private static final Comparator<Workout.Point> POINT_COMPARATOR = new Comparator<Workout.Point>() {
        public int compare(final Workout.Point o1, final Workout.Point o2) {
            return Double.compare(o1.distance, o2.distance);
        }
    };

    private NikeCurveHelper() {
    }

    public static byte[] getPNGImage(final String userId, final String workoutId) {
        final Workout workout = getNikeCurveWorkout(userId, workoutId);
        return NikeGraphDrawer.getPNGImage(workout);
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
        try {
            final Node root = getRoot(userId, workoutId);
            final SortedSet<Workout.Point> points = new TreeSet<Workout.Point>(POINT_COMPARATOR);
            final Node extended = (Node) EXTENDED_DATA.evaluate(root, XPathConstants.NODE);
            registerExtandedLowPass(points, extended);
            runningAverageLowPassFilter(radius, points);
            return convertToDisplay(points);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    private static String convertToDisplay(final SortedSet<Workout.Point> points) {
        for (final Workout.Point point : points) {
            point.pace = -point.pace / 60000; //go to minutes/km
        }
        return points.toString();
    }

    private static void runningAverageLowPassFilter(final int radius, final Collection<Workout.Point> points) {
        final List<Workout.Point> pristine = new ArrayList<Workout.Point>(points);
        int index = 0;
        for (final Workout.Point point : points) {
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

    private static void registerExtandedLowPass(final Set<Workout.Point> points, final Node extendedDataNode) throws
            XPathExpressionException {
        final double sampling = getDouble(INTERVAL, extendedDataNode);
        final String extendedData = TEXT.evaluate(extendedDataNode);
        double previousDistance = 0.0;
        final double samplingMilisec = sampling * 1000;
        for (final String fragment : ("0," + extendedData).split(",")) {
            final double distance = parseDouble(fragment);
            final double pace = samplingMilisec / (distance - previousDistance);
            if (!Double.isInfinite(pace) && !Double.isNaN(pace)) {
                points.add(new Workout.Point(distance, pace));
            }
            previousDistance = distance;
        }
    }

    private static double getDouble(final XPathExpression interval, final Node node) throws XPathExpressionException {
        return parseDouble(interval.evaluate(node));
    }

    public static String getNikePlusCurve(final String userId, final String workoutId) {
        final Workout workout = getNikeCurveWorkout(userId, workoutId);
        return convertToDisplay(workout.points);
    }

    private static Workout getNikeCurveWorkout(final String userId, final String workoutId) {
        final SortedSet<Workout.Point> points = new TreeSet<Workout.Point>(POINT_COMPARATOR);
        try {
            final Node root = getRoot(userId, workoutId);
            final double totalDistance = getDouble(TOTAL_DISTANCE, root);
            final Node extended = (Node) EXTENDED_DATA.evaluate(root, XPathConstants.NODE);
            final Config config = new Config(totalDistance / MAX_FINAL_POINTS);
            registerExtanded(points, extended, config);
            final TreeSet<Workout.Point> snaps = new TreeSet<Workout.Point>(POINT_COMPARATOR);
            //order is important !
            addSnapshot(root, points, snaps, KM_SNAPSHOTS, config);
            addSnapshot(root, points, snaps, CLICK_SNAPSHOTS, config);
            points.addAll(snaps);
            points.last().pace = points.first().pace;
            dumpPoints(points, config);
            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            final Date date = format.parse(DATE.evaluate(root));
            return new Workout(UserHelper.getLogin(userId), date, points, snaps, totalDistance,
                    (long) getDouble(TOTAL_DURATION, root) / 1000,
                    (long) getDouble(TOTAL_ENERGY, root), config.minPace, config.maxPace);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static Node getRoot(final String userId, final String workoutId) throws XPathExpressionException {
        return (Node) ROOT.evaluate(getWorkoutData(userId, workoutId), XPathConstants.NODE);
    }

    private static void registerExtanded(final Set<Workout.Point> points, final Node extendedDataNode,
                                         final Config config) throws XPathExpressionException {
        final double sampling = getDouble(INTERVAL, extendedDataNode);
        final String extendedData = TEXT.evaluate(extendedDataNode);
        double previousTime = 0.0;
        double previousDistance = 0.0;
        final List<String> distances = new ArrayList<String>(Arrays.asList(extendedData.split(",")));
        if (parseDouble(distances.get(0)) != 0)
            distances.add(0, "0");
        boolean first = true;
        for (int i = 0; i < distances.size(); i++) {
            final StringBuilder log = new StringBuilder();
            final int time = (int) (i * sampling * 1000);
            final String fragment = distances.get(i);
            final double distance = parseDouble(fragment);
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
                points.add(new Workout.Point(distance, pace));
                log.append(" (selected)");
                if (first) {
                    points.add(new Workout.Point(0, pace));
                    first = false;
                }
            }
            logText(log.toString());
            previousDistance = distance;
            previousTime = time;
        }
    }

    private static void dumpPoints(final Set<Workout.Point> points, final Config config) {
        logText("points: " + points.size());
        logText("min: " + config.minPace + "\tmax: " + config.maxPace);
        int i = 0;
        for (final Workout.Point point : points) {
            logText("point " + i + "\t" + "pace: " + (int) point.pace + " ms/km\tdist: " + point.distance + " km");
            i++;
        }
    }

    private static void logText(final String text) {
        //noinspection ConstantIfStatement
        if (false) {
            System.out.println(text);
        }
    }

    private static void addSnapshot(final Object root, final Set<Workout.Point> points,
                                    final TreeSet<Workout.Point> snaps, final XPathExpression query,
                                    final Config config) throws XPathExpressionException {
        final NodeList snapshots = (NodeList) query.evaluate(root, XPathConstants.NODESET);
        for (int i = 0; i < snapshots.getLength(); i++) {
            final Node snapShotNode = snapshots.item(i);
            final double pace = getDouble(PACE, snapShotNode);
            final double distance = getDouble(DISTANCE, snapShotNode);
            final String event = EVENT.evaluate(snapShotNode);
            final Workout.Point snap = new Workout.Point(distance, pace);
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
    private static boolean isFarEnough(final TreeSet<Workout.Point> snaps, final Workout.Point snap,
                                       final Config config) {
        for (final Workout.Point point : snaps) {
            if (isclose(config, point, snap))
                return false;
        }
        return true;
    }

    //to optimise, the tree is ordered
    private static void removeClosePoints(final Set<Workout.Point> points, final Workout.Point snap,
                                          final Config config) {
        for (Iterator<Workout.Point> it = points.iterator(); it.hasNext();) {
            final Workout.Point point1 = it.next();
            if (isclose(config, point1, snap)) {
                it.remove();
                twistPace(snap, point1, config);
            }
        }
    }

    private static void twistPace(final Workout.Point snap, final Workout.Point point, final Config config) {
        if (point.pace == config.maxPace || point.pace == config.minPace)
            snap.pace = point.pace;
    }

    private static boolean isclose(final Config config, final Workout.Point point1, final Workout.Point point) {
        return Math.abs(point.distance - point1.distance) < config.distanceRange;
    }
}
