package com.nraynaud.sport.hibernate;

import com.nraynaud.sport.Track;
import com.nraynaud.sport.User;
import com.nraynaud.sport.UserString;
import com.nraynaud.sport.UserStringImpl;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TRACKS")
public class TrackImpl implements Track {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @GeneratedValue
    @Column(name = "CREATION_DATE")
    private Date creationDate;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "POINTS")
    private String points;

    @Column(name = "LENGTH")
    private double length;

    @ManyToOne(targetEntity = UserImpl.class)
    @JoinColumn(name = "OWNER_ID", nullable = false, updatable = false)
    private User user;

    public Long getId() {
        return id;
    }

    public UserString getTitle() {
        return UserStringImpl.valueOf(title);
    }

    public User getUser() {
        return user;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(final String points) {
        this.points = points;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public double getLength() {
        return length;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    @SuppressWarnings({"NonFinalFieldReferenceInEquals"})
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof TrackImpl)) return false;
        final TrackImpl track = (TrackImpl) o;
        return id.equals(track.id);
    }

    @SuppressWarnings({"NonFinalFieldReferencedInHashCode"})
    public int hashCode() {
        return id.hashCode();
    }
}
