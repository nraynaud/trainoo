package com.nraynaud.sport.data;

public class DisciplineCount extends DisciplineDistance<DisciplineDistance.CountAndDistance> {
    public DisciplineCount(final String discipline, final Long count) {
        super(discipline, new CountAndDistance(count));
    }
}
