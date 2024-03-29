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

    @Column(name = "ENERGY")
    private Long energy;

    @Column(name = "DISCIPLINE", nullable = false)
    private String discipline;

    @Column(name = "NIKEPLUSID", unique = true)
    private String nikePlusId;

    @Column(name = "DEBRIEFING")
    private String debriefing;

    @ManyToOne(targetEntity = UserImpl.class)
    @JoinColumn(name = "USER_ID", nullable = false, updatable = false)
    private User user;

    @SuppressWarnings({"UnusedDeclaration"})
    @OneToMany(targetEntity = PublicMessageImpl.class, mappedBy = "workout")
    private Collection<PublicMessage> publicMessages;

    @SuppressWarnings({"UnusedDeclaration"})
    @ManyToMany(targetEntity = UserImpl.class, fetch = FetchType.EAGER)
    @JoinTable(name = "WORKOUT_USER",
            joinColumns = @JoinColumn(name = "WORKOUT_ID", nullable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "USER_ID", nullable = false, updatable = false))
    private Collection<User> participants;

    transient Long messageCount;

    @ManyToOne(targetEntity = TrackImpl.class)
    @JoinColumn(name = "TRACK_ID", nullable = true, updatable = true)
    private Track track;

    public WorkoutImpl() {
    }

    public WorkoutImpl(final User user,
                       final Date date,
                       final Long duration,
                       final Double distance,
                       final Long energy,
                       final String discipline,
                       final String debriefing,
                       final String nikePlusId) {
        this.date = date;
        this.user = user;
        this.duration = duration;
        this.distance = distance;
        this.energy = energy;
        this.discipline = discipline;
        this.debriefing = debriefing;
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

    public Long getMessageCount() {
        return messageCount != null ? messageCount : Long.valueOf(0);
    }

    public void setMessageCount(final Long messageCount) {
        this.messageCount = messageCount;
    }

    public Collection<PublicMessage> getMessages() {
        return publicMessages;
    }

    public Collection<User> getParticipants() {
        return participants;
    }

    public boolean isNikePlus() {
        return nikePlusId != null;
    }

    public String getNikePlusId() {
        return nikePlusId;
    }

    public UserString getDebriefing() {
        return UserStringImpl.valueOf(debriefing);
    }

    public void setDebriefing(final String debriefing) {
        this.debriefing = debriefing;
    }

    public Long getEnergy() {
        return energy;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(final Track track) {
        this.track = track;
    }

    public void setEnergy(final Long energy) {
        this.energy = energy;
    }
}
