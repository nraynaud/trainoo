package com.nraynaud.sport.hibernate;

import com.nraynaud.sport.Message;
import com.nraynaud.sport.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "MESSAGES")
public class MessageImpl implements Message {
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
    @JoinColumn(name = "RECEIVER_ID", nullable = false, updatable = false)
    private User receiver;

    @Column(name = "DATE", nullable = false)
    @OrderBy("ASC")
    private Date date;

    public MessageImpl() {
    }

    public MessageImpl(final User sender, final User receiver, final Date date, final String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.date = date;
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

    public User getReceiver() {
        return receiver;
    }
}
