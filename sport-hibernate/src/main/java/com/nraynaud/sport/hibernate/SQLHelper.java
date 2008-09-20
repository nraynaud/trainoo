package com.nraynaud.sport.hibernate;

import com.nraynaud.sport.Workout;
import com.nraynaud.sport.data.PaginatedCollection;

import javax.persistence.Query;
import java.util.*;

public class SQLHelper {
    static final List<String> EMPTY_STRING_LIST = Collections.emptyList();

    private SQLHelper() {
    }

    public static <T> Predicate createInListPredicate(final String leftPart, final Collection<T> values,
                                                      final String variablePrefix) {
        if (values.size() == 1)
            return createEqualsPredicate(leftPart, values.iterator().next(), variablePrefix);
        final StringBuilder clause = new StringBuilder(20);
        for (int i = 0; i < values.size(); i++)
            (i > 0 ? clause.append(", ") : clause).append(':').append(variablePrefix).append(i);
        final String text = " IN (" + clause + ")";
        return new Predicate() {
            public String sql() {
                if (values.size() > 0)
                    return leftPart + text;
                else
                    return "";
            }

            public void bindVariables(final Query query) {
                int i = 0;
                for (final T value : values) {
                    query.setParameter(variablePrefix + i, value);
                    i++;
                }
            }
        };
    }

    private static <T> Predicate createEqualsPredicate(final String leftPart, final T value,
                                                       final String variablePrefix) {
        return new Predicate() {
            public String sql() {
                return leftPart + " = :" + variablePrefix;
            }

            public void bindVariables(final Query query) {
                query.setParameter(variablePrefix, value);
            }
        };
    }

    public static PaginatedCollection<Workout> paginateWorkoutQuery(final int firstIndex, final int pageSize,
                                                                    final Query query) {
        final List<Object[]> result = paginatedQuery(pageSize, firstIndex, query);
        final List<Workout> list = new ArrayList<Workout>(result.size());
        for (final Object[] row : result) {
            final WorkoutImpl workout = (WorkoutImpl) row[0];
            workout.setMessageCount(((Number) row[1]).longValue());
            list.add(workout);
        }
        return paginateList(firstIndex, pageSize, list);
    }

    public static <T> PaginatedCollection<T> paginateList(final int startIndex, final int pageSize,
                                                          final List<T> list) {
        return new PaginatedCollection<T>() {
            public boolean hasPrevious() {
                return list.size() > pageSize;
            }

            public boolean hasNext() {
                return startIndex > 0;
            }

            public int getPreviousIndex() {
                return startIndex + pageSize;
            }

            public int getNextIndex() {
                return startIndex - pageSize;
            }

            public boolean isEmpty() {
                return list.isEmpty();
            }

            public Iterator<T> iterator() {
                return list.subList(0, list.size() > pageSize ? pageSize : list.size()).iterator();
            }
        };
    }

    static <T> List<T> paginatedQuery(final int pageSize, final int startIndex, final Query query) {
        query.setMaxResults(pageSize + 1);
        query.setFirstResult(startIndex);
        //noinspection unchecked
        return query.getResultList();
    }

    static <T> PaginatedCollection<T> emptyPage() {
        return paginateList(0, 1, Collections.<T>emptyList());
    }

    static <T> PaginatedCollection<T> paginateQuery(final int pageSize, final int startIndex,
                                                    final Query query) {
        final List<T> list = paginatedQuery(pageSize, startIndex, query);
        return paginateList(startIndex, pageSize, list);
    }

    public abstract static class Predicate {
        public String toString() {
            return sql();
        }

        public abstract String sql();

        public abstract void bindVariables(Query query);
    }
}
