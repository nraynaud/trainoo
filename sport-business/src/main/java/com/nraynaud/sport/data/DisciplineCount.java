package com.nraynaud.sport.data;

public class DisciplineCount extends DisciplineData<DisciplineData.CountAndDistance> {
    public DisciplineCount(final String discipline, final Long count) {
        super(discipline, new CountAndDistance(count));
    }
}
