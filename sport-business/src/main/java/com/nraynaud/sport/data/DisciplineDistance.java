package com.nraynaud.sport.data;

import com.nraynaud.sport.UserString;
import com.nraynaud.sport.UserStringImpl;

public class DisciplineDistance<T> {
    public final UserString discipline;
    public final T data;

    public DisciplineDistance(final String discipline, final T data) {
        this.discipline = UserStringImpl.valueOf(discipline);
        this.data = data;
    }

    public String toString() {
        return discipline.toString();
    }

    public static class CountAndDistance {
        public final Long count;

        public CountAndDistance(final Long count) {
            this.count = count;
        }
    }
}
