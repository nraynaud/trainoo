package com.nraynaud.sport.data;

import com.nraynaud.sport.UserString;
import com.nraynaud.sport.UserStringImpl;

public class DisciplineDistance {
    public final UserString discipline;

    public final Double distance;

    public DisciplineDistance(final String discipline, final Double distance) {
        this.discipline = UserStringImpl.valueOf(discipline);
        this.distance = distance;
    }
}
