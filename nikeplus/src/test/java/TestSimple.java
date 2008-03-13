import com.nraynaud.sport.importer.FailureException;
import com.nraynaud.sport.importer.WorkoutCollector;
import com.nraynaud.sport.nikeplus.NikePlusExtractor;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class TestSimple {

    @Test
    public void testSimple() throws IOException, FailureException, ParseException {
        final String login = "nike@nraynaud.com";
        final String password = "lol";
        final WorkoutCollector collector = new WorkoutCollector() {

            public void collectWorkout(final String nikePlusId, final String discipline, final Date date,
                                       final Double distance,
                                       final Long duration) {
                System.out.println("distance: " + distance);
                System.out.println("duration: " + duration);
                System.out.println("date: " + date);
            }

            public void endCollection() {
            }
        };
        NikePlusExtractor.collectNikePlusWorkouts(login, password, collector);
        final String testValue = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<plusService>\n"
                + "    <status>success</status>\n"
                + "    <runList>\n"
                + "        <run id=\"1821241935\">\n"
                + "            <startTime>2007-02-03T16:36:41+01:00</startTime>\n"
                + "            <distance>20.0191</distance>\n"
                + "            <duration>5874045</duration>\n"
                + "            <syncTime>2007-02-03T17:46:44+00:00</syncTime>\n"
                + "            <calories>1657</calories>\n"
                + "            <name><![CDATA[]]></name>\n"
                + "            <description><![CDATA[]]></description>\n"
                + "        </run>\n"
                + "        <run id=\"1149957062\">\n"
                + "            <startTime>2007-02-11T16:59:12+01:00</startTime>\n"
                + "            <distance>10.017</distance>\n"
                + "            <duration>2843296</duration>\n"
                + "            <syncTime>2007-02-11T17:03:27+00:00</syncTime>\n"
                + "            <calories>829</calories>\n"
                + "            <name><![CDATA[]]></name>\n"
                + "            <description><![CDATA[]]></description>\n"
                + "        </run>\n"
                + "        <run id=\"2088931662\">\n"
                + "            <startTime>2007-03-25T16:41:38+01:00</startTime>\n"
                + "            <distance>20.0062</distance>\n"
                + "            <duration>5251273</duration>\n"
                + "            <syncTime>2007-03-25T17:56:42+00:00</syncTime>\n"
                + "            <calories>1584</calories>\n"
                + "            <name><![CDATA[]]></name>\n"
                + "            <description><![CDATA[]]></description>\n"
                + "        </run>\n"
                + "    </runList>\n"
                + "</plusService>";
        final AtomicInteger runCount = new AtomicInteger(0);
        NikePlusExtractor.extractWorkouts(testValue.getBytes("UTF-8"), new WorkoutCollector() {

            public void collectWorkout(final String nikePlusId, final String discipline, final Date date,
                                       final Double distance,
                                       final Long duration) {
                runCount.incrementAndGet();
            }

            public void endCollection() {
            }
        });
        Assert.assertEquals(3, runCount.intValue());
    }
}
