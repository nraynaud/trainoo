package com.nraynaud.sport.hibernate;

import com.nraynaud.sport.data.DisciplineData;

import java.util.List;

public class DisciplineAggregator<T> {
    public static final DisciplineAggregator<DisciplineData.Count> COUNT_AGGREGATOR = new DisciplineAggregator<DisciplineData.Count>(
            "new com.nraynaud.sport.data.DisciplineCount(w.discipline, count(*))");

    public final String projectionPart;

    DisciplineAggregator(final String projectionPart) {
        this.projectionPart = projectionPart;
    }

    @SuppressWarnings({"unchecked"})
    public List<DisciplineData<T>> castqueryResult(final List<?> result) {
        return (List<DisciplineData<T>>) result;
    }
}
