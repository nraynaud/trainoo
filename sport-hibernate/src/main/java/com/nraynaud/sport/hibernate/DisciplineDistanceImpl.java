package com.nraynaud.sport.hibernate;

import com.nraynaud.sport.StatisticsPageData;

public class DisciplineDistanceImpl implements StatisticsPageData.DisciplineDistance {
    private final String discipline;
    private final Double distance;

    public DisciplineDistanceImpl(final String discipline, final Double distance) {
        this.discipline = discipline;
        this.distance = distance;
    }

    public String getDiscipline() {
        return discipline;
    }

    public Double getDistance() {
        return distance;
    }
}
