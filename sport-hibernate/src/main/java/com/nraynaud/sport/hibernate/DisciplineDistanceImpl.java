package com.nraynaud.sport.hibernate;

import com.nraynaud.sport.StatisticsPageData;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DisciplineDistanceImpl implements StatisticsPageData.DisciplineDistance {
    @Id
    private String discipline;
    private Double distance;

    public String getDiscipline() {
        return discipline;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDiscipline(final String discipline) {
        this.discipline = discipline;
    }

    public void setDistance(final Double distance) {
        this.distance = distance;
    }
}
