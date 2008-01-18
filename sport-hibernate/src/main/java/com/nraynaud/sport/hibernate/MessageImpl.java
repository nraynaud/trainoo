package com.nraynaud.sport.hibernate;

import com.nraynaud.sport.Message;
import com.nraynaud.sport.User;

import javax.persistence.*;

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
    @JoinColumn(name = "USER_ID", nullable = false, updatable = false)
    private User user;

    public MessageImpl() {
    }

    public MessageImpl(final User user, final String content) {
        this.user = user;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public String content() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }
}
