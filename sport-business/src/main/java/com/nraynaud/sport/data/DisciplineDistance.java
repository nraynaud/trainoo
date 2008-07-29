package com.nraynaud.sport.data;

import com.nraynaud.sport.UserString;
import com.nraynaud.sport.UserStringImpl;

public class DisciplineDistance {
    public final UserString discipline;

    public final Double distance;
    public final Long count;

    public DisciplineDistance(final String discipline, final Double distance, final Long count) {
        this.count = count;
        this.discipline = UserStringImpl.valueOf(discipline);
        this.distance = distance;
    }

    public String toString() {
        return discipline.toString();
    }
}
