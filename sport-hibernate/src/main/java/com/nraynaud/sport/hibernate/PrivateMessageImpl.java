package com.nraynaud.sport.hibernate;

import com.nraynaud.sport.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "MESSAGES")
public class PrivateMessageImpl implements PrivateMessage {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "CONTENT")
    private String content;

    @ManyToOne(targetEntity = UserImpl.class)
    @JoinColumn(name = "SENDER_ID", nullable = false, updatable = false)
    private User sender;
    @ManyToOne(targetEntity = UserImpl.class)
    @JoinColumn(name = "RECEIVER_ID", nullable = true, updatable = false)
    private User receiver;

    @ManyToOne(targetEntity = UserImpl.class)
    @JoinColumn(name = "DELETED_BY", nullable = true)
    private User deleter;

    @Column(name = "DATE", nullable = false)
    @OrderBy
    private Date date;

    @ManyToOne(targetEntity = WorkoutImpl.class)
    @JoinColumn(name = "WORKOUT_ID", updatable = false)
    private Workout workout;

    @Column(name = "IS_READ")
    private boolean read;

    private transient boolean isNew;

    public PrivateMessageImpl() {
    }

    public PrivateMessageImpl(final User sender,
                              final User receiver,
                              final Workout workout,
                              final Date date,
                              final String content) {
        this(sender, receiver, date, content);
        this.workout = workout;
    }

    public PrivateMessageImpl(final User sender, final User receiver, final Date date, final String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.date = date;
    }

    public PrivateMessageImpl(final User sender,
                              final User receiver,
                              final Date date,
                              final String content,
                              final Workout workout) {
        this(sender, receiver, date, content);
        this.workout = workout;
    }

    public PrivateMessageImpl(final User sender, final Date date, final String content, final WorkoutImpl workout) {
        this.sender = sender;
        this.content = content;
        this.date = date;
        this.workout = workout;
    }

    public Long getId() {
        return id;
    }

    public UserString getContent() {
        return UserStringImpl.valueOf(content);
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

    public User get() {
        return receiver;
    }

    public Workout getWorkout() {
        return workout;
    }

    public boolean canWrite(final User user) {
        return user != null && (user.equals(sender) || user.equals(receiver));
    }

    public MessageKind getMessageKind() {
        return MessageKind.PRIVATE;
    }

    public User getReceiver() {
        return receiver;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(final boolean aNew) {
        isNew = aNew;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(final boolean read) {
        this.read = read;
    }

    public User getDeleter() {
        return deleter;
    }
}
