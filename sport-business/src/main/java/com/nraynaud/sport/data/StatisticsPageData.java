package com.nraynaud.sport.data;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

public class StatisticsPageData {
    public final double totalDistance;
    public final Collection<PeriodData<DistanceAndDuration>> distanceByYear;
    public final Collection<PeriodData<DistanceAndDuration>> distanceByMonth;

    public StatisticsPageData(final double totalDistance,
                              final Collection<?> distanceByYear,
                              final Collection<?> distanceByMonth) {
        this.totalDistance = totalDistance;
        this.distanceByYear = convert(distanceByYear, new RowConverter<PeriodData<DistanceAndDuration>>() {
            public PeriodData<DistanceAndDuration> convert(final Object[] n) {
                return new PeriodData<DistanceAndDuration>(String.valueOf(n[0]),
                        new DistanceAndDuration(numberToDouble(n[1]), numberToLong(n[2])));
            }
        });
        this.distanceByMonth = convert(distanceByMonth, new RowConverter<PeriodData<DistanceAndDuration>>() {
            public PeriodData<DistanceAndDuration> convert(final Object[] n) {
                return new PeriodData<DistanceAndDuration>(String.valueOf(n[1]) + '/' + String.valueOf(n[0]),
                        new DistanceAndDuration(numberToDouble(n[2]), numberToLong(n[3])));
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

    public static class DistanceAndDuration {
        public final Double distance;
        public final Long duration;

        public DistanceAndDuration(final Double distance, final Long duration) {
            this.distance = distance;
            this.duration = duration;
        }
    }

    public interface RowConverter<T> {
        public T convert(Object[] row);
    }
}
