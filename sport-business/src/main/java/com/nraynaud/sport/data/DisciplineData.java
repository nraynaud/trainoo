package com.nraynaud.sport.data;

import com.nraynaud.sport.UserString;
import com.nraynaud.sport.UserStringImpl;

public class DisciplineData<T> {
    public final UserString discipline;
    public final T data;

    public DisciplineData(final String discipline, final T data) {
        this.discipline = UserStringImpl.valueOf(discipline);
        this.data = data;
    }

    public String toString() {
        return discipline.toString();
    }

    public static class Count {
        public final Long count;

        public Count(final Long count) {
            this.count = count;
        }
    }
}
