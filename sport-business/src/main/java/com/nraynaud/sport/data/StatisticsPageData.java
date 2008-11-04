package com.nraynaud.sport.data;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;

public class StatisticsPageData {
    public final double totalDistance;
    public final Collection<PeriodData<Double>> distanceByYear;
    public final Collection<PeriodData<Double>> distanceByMonth;

    public StatisticsPageData(final double totalDistance,
                              final Collection<?> distanceByYear,
                              final Collection<?> distanceByMonth) {
        this.totalDistance = totalDistance;
        this.distanceByYear = convert(distanceByYear, new RowConverter<PeriodData<Double>>() {
            public PeriodData<Double> convert(final Object[] n) {
                return new PeriodData<Double>(String.valueOf(n[0]), zeroIfNull(n[1]));
            }
        });
        this.distanceByMonth = convert(distanceByMonth, new RowConverter<PeriodData<Double>>() {
            public PeriodData<Double> convert(final Object[] n) {
                return new PeriodData<Double>(String.valueOf(n[1]) + '/' + String.valueOf(n[0]),
                        zeroIfNull(n[2]));
            }
        });
    }

    private static double zeroIfNull(final Object o) {
        return o == null ? 0 : ((Number) o).doubleValue();
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

    public interface RowConverter<T> {
        public T convert(Object[] row);
    }
}
