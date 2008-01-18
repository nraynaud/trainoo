package com.nraynaud.sport.hibernate;

import com.nraynaud.sport.User;
import com.nraynaud.sport.Workout;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "WORKOUTS")
public class WorkoutImpl implements Workout {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "WORKOUT_DATE", nullable = false)
    @OrderBy("DESC")
    private Date date;

    @Column(name = "DISTANCE")
    private Double distance;

    @Column(name = "DURATION")
    private Long duration;

    @Column(name = "DISCIPLINE", nullable = false)
    private String discipline;

    @ManyToOne(targetEntity = UserImpl.class)
    @JoinColumn(name = "USER_ID", nullable = false, updatable = false)
    private User user;

    public WorkoutImpl() {
    }

    public WorkoutImpl(final User user,
                       final Date date,
                       final Long duration,
                       final Double distance,
                       final String discipline) {
        this.date = date;
        this.user = user;
        this.duration = duration;
        this.distance = distance;
        this.discipline = discipline;
    }

    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(final Double distance) {
        this.distance = distance;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(final Long duration) {
        this.duration = duration;
    }

    public String toString() {
        return date.toString();
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(final String discipline) {
        this.discipline = discipline;
    }
}
