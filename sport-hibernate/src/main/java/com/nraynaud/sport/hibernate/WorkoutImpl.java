package com.nraynaud.sport.hibernate;

import com.nraynaud.sport.*;

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

    @Column(name = "NIKEPLUSID")
    private String nikePlusId;

    @ManyToOne(targetEntity = UserImpl.class)
    @JoinColumn(name = "USER_ID", nullable = false, updatable = false)
    private User user;

    @OneToMany(targetEntity = PublicMessageImpl.class, mappedBy = "workout")
    private Collection<PublicMessage> publicMessages;

    @ManyToMany(targetEntity = UserImpl.class, fetch = FetchType.EAGER)
    @JoinTable(name = "WORKOUT_USER",
            joinColumns = @JoinColumn(name = "WORKOUT_ID", nullable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "USER_ID", nullable = false, updatable = false))
    private Collection<User> participants;

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

    public WorkoutImpl(final User user,
                       final Date date,
                       final Long duration,
                       final Double distance,
                       final String discipline,
                       final String nikePlusId) {
        this.date = date;
        this.user = user;
        this.duration = duration;
        this.distance = distance;
        this.discipline = discipline;
        this.nikePlusId = nikePlusId;
    }

    public Long getId() {
        return id;
    }

    public Kind getKind() {
        return Kind.WORKOUT;
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

    public UserString getDiscipline() {
        return UserStringImpl.valueOf(discipline);
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

    public Collection<User> getParticipants() {
        return participants;
    }
}
