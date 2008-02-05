package com.nraynaud.sport.hibernate;

import com.nraynaud.sport.PublicMessage;
import com.nraynaud.sport.User;
import com.nraynaud.sport.Workout;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PUBLIC_MESSAGES")
public class PublicMessageImpl implements PublicMessage {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "CONTENT")
    private String content;

    @ManyToOne(targetEntity = UserImpl.class)
    @JoinColumn(name = "SENDER_ID", nullable = false, updatable = false)
    private User sender;

    @Column(name = "DATE", nullable = false)
    @OrderBy
    private Date date;

    @ManyToOne(targetEntity = WorkoutImpl.class)
    @JoinColumn(name = "WORKOUT_ID", updatable = false)
    private Workout workout;

    private transient boolean isNew;

    public PublicMessageImpl() {
    }

    public PublicMessageImpl(final User sender, final Date date, final String content) {
        this.sender = sender;
        this.content = content;
        this.date = date;
    }

    public PublicMessageImpl(final User sender,
                             final Date date,
                             final String content,
                             final Workout workout) {
        this(sender, date, content);
        this.workout = workout;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public User getSender() {
        return sender;
    }

    public Date getDate() {
        return date;
    }

    public Workout getWorkout() {
        return workout;
    }

    public boolean canDelete(final User user) {
        return user != null && user.equals(sender);
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(final boolean aNew) {
        isNew = aNew;
    }
}