package com.nraynaud.sport.hibernate;

import com.nraynaud.sport.StatisticsPageData;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DisciplineDistanceImpl implements StatisticsPageData.DisciplineDistance {
    @Id
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
