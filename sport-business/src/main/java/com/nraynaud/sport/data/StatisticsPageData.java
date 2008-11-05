package com.nraynaud.sport.data;

import com.nraynaud.sport.User;
import com.nraynaud.sport.UserString;
import com.nraynaud.sport.UserStringImpl;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

public class StatisticsPageData {
    public final User user;
    public final Collection<UserString> userDisciplines;
    public final Double totalDistance;
    public final Collection<PeriodData<WorkoutStat>> distanceByYear;
    public final Collection<PeriodData<WorkoutStat>> distanceByMonth;

    public StatisticsPageData(final User user, final Collection<?> userDisciplines, final Object totalDistance,
                              final Collection<?> distanceByYear,
                              final Collection<?> distanceByMonth) {
        this.user = user;
        this.userDisciplines = convert(userDisciplines, new RowConverter<UserString>() {
            public UserString convert(final Object[] row) {
                return UserStringImpl.valueOf((String) row[0]);
            }
        });
        this.totalDistance = numberToDouble(totalDistance);
        this.distanceByYear = convert(distanceByYear, new RowConverter<PeriodData<WorkoutStat>>() {
            public PeriodData<WorkoutStat> convert(final Object[] n) {
                return new PeriodData<WorkoutStat>(String.valueOf(n[0]),
                        new WorkoutStat(numberToDouble(n[1]), numberToLong(n[2]), numberToLong(n[3])));
            }
        });
        this.distanceByMonth = convert(distanceByMonth, new RowConverter<PeriodData<WorkoutStat>>() {
            public PeriodData<WorkoutStat> convert(final Object[] n) {
                return new PeriodData<WorkoutStat>(String.valueOf(n[1]) + '/' + String.valueOf(n[0]),
                        new WorkoutStat(numberToDouble(n[2]), numberToLong(n[3]), numberToLong(n[4])));
            }
        });
    }

    private static Double numberToDouble(final Object o) {
        return o == null ? null : ((Number) o).doubleValue();
    }

    private static Long numberToLong(final Object o) {
        return o == null ? null : ((Number) o).longValue();
    }

    public static <T> Collection<T> convert(final Collection<?> input, final RowConverter<T> converter) {
        return new AbstractCollection<T>() {
            public Iterator<T> iterator() {
                return new Iterator<T>() {
                    private final Iterator<?> wrapped = input.iterator();

                    public boolean hasNext() {
                        return wrapped.hasNext();
                    }

                    public T next() {
                        return converter.convert((Object[]) wrapped.next());
                    }

                    public void remove() {
                        wrapped.remove();
                    }
                };
            }

            public int size() {
                return input.size();
            }
        };
    }

    public static class PeriodData<T> {
        public final String period;
        public final T data;

        public PeriodData(final String period, final T data) {
            this.period = period;
            this.data = data;
        }
    }

    public static class WorkoutStat {
        public final Double distance;
        public final Long duration;
        public final Long energy;

        public WorkoutStat(final Double distance, final Long duration, final Long energy) {
            this.distance = distance;
            this.duration = duration;
            this.energy = energy;
        }
    }

    public interface RowConverter<T> {
        public T convert(Object[] row);
    }
}
