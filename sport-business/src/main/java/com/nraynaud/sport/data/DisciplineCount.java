package com.nraynaud.sport.data;

public class DisciplineCount extends DisciplineData<DisciplineData.Count> {
    public DisciplineCount(final String discipline, final Long count) {
        super(discipline, new Count(count));
    }
}
