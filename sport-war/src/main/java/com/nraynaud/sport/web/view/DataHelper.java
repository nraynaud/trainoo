package com.nraynaud.sport.web.view;

import com.nraynaud.sport.Workout;
import com.nraynaud.sport.formatting.DistanceIO;
import com.nraynaud.sport.formatting.FormatHelper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class DataHelper {
    private DataHelper() {
    }

    public static List<Data> compute(final Workout workout) {
        final ArrayList<Data> list = new ArrayList<Data>();
        final Workout workoutWrapper = createWorkoutWrapper(workout);
        for (final DataComputer computer : DataComputer.values())
            try {
                list.add(computer.compute(workoutWrapper));
            } catch (NoDataException e) {
                // do nothing, go back on track
            }
        return list;
    }

    private static Workout createWorkoutWrapper(final Workout workout) {
        return (Workout) Proxy.newProxyInstance(Workout.class.getClassLoader(),
                new Class<?>[]{Workout.class}, new InvocationHandler() {
            public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
                final Object result = method.invoke(workout, args);
                if (result == null)
                    throw NO_DATA_EXCEPTION;
                return result;
            }
        });
    }

    private static Data computeAverageSpeed(final Workout workout) {
        final double distance = workout.getDistance().doubleValue();
        final long duration = workout.getDuration().longValue();
        final String value = DistanceIO.formatDistance(distance / (duration / 60.0 / 60)) + "<small>km/h</small>";
        final double averageTimeByKM = duration / distance;
        final String value2 = " <small>("
                + (int) (averageTimeByKM / 60)
                + "'&nbsp;"
                + (int) (averageTimeByKM % 60)
                + "/km)</small>";
        return new Data("Vitesse Moyenne&nbsp;:", value + value2, false);
    }

    private static final RuntimeException NO_DATA_EXCEPTION = new NoDataException();

    private static class NoDataException extends RuntimeException {
    }

    private enum DataComputer {
        DISTANCE {
            public Data compute(final Workout workout) {
                return new Data("Distance&nbsp;:", FormatHelper.formatDistanceHtml(workout.getDistance(), ""), true);
            }},
        DURATION {
            public Data compute(final Workout workout) {
                return new Data("Durée&nbsp;:", FormatHelper.formatDuration(workout.getDuration(), ""), true);
            }},
        AVERAGE_SPEED {
            public Data compute(final Workout workout) {
                return computeAverageSpeed(workout);
            }},
        ENERGY {
            public Data compute(final Workout workout) {
                return new Data("Énergie Dépensée&nbsp;:", workout.getEnergy() + "<small>kcal</small>", true);
            }},
        AVERAGE_POWER {
            public Data compute(final Workout workout) {
                return new Data("Puissance Moyenne&nbsp;:",
                        workout.getEnergy().longValue() * 4187 / workout.getDuration().longValue() + "<small>W</small>",
                        false);
            }};

        public abstract Data compute(Workout workout);
    }

    public static class Data {
        public final String label;
        public final String value;
        public final boolean userProvided;

        public Data(final String label, final String value, final boolean userProvided) {
            this.label = label;
            this.value = value;
            this.userProvided = userProvided;
        }
    }
}
