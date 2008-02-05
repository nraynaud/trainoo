package com.nraynaud.sport.hibernate;

import com.nraynaud.sport.PublicMessage;
import com.nraynaud.sport.User;
import com.nraynaud.sport.Workout;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "WORKOUTS")
public class WorkoutImpl implements Workout {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "WORKOUT_DATE", nullable = false)
    @OrderBy
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

    @OneToMany(targetEntity = PublicMessageImpl.class, mappedBy = "workout")
    private Collection<PublicMessage> publicMessages;

    transient Long messageNumber;

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

    public Long getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(final Long messageNumber) {
        this.messageNumber = messageNumber;
    }

    public Collection<PublicMessage> getMessages() {
        return publicMessages;
    }
}
