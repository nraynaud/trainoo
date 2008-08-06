package com.nraynaud.sport.hibernate;

import javax.persistence.Query;
import java.util.Collection;

public class SQLHelper {
    private SQLHelper() {
    }

    public static Predicate createInListPredicate(final String leftPart, final Collection<String> values,
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
                for (final String value : values) {
                    query.setParameter(variablePrefix + i, value);
                    i++;
                }
            }
        };
    }

    private static Predicate createEqualsPredicate(final String leftPart, final String value,
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

    public abstract static class Predicate {
        public String toString() {
            return sql();
        }

        public abstract String sql();

        public abstract void bindVariables(Query query);
    }
}
