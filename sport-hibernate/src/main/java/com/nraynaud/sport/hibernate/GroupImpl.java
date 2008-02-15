package com.nraynaud.sport.hibernate;

import com.nraynaud.sport.Group;
import com.nraynaud.sport.User;
import com.nraynaud.sport.UserString;
import com.nraynaud.sport.UserStringImpl;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "GROUPS")
public class GroupImpl implements Group {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CREATION_DATE")
    private Date creationDate;

    @ManyToOne(targetEntity = UserImpl.class)
    @JoinColumn(name = "OWNER_ID", nullable = false, updatable = false)
    private User owner;

    @ManyToMany(targetEntity = UserImpl.class, fetch = FetchType.LAZY)
    @JoinTable(name = "GROUP_USER", joinColumns = @JoinColumn(name = "GROUP_ID", nullable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "USER_ID", nullable = false, updatable = false))
    private Collection<UserImpl> members;

    public GroupImpl() {
    }

    public GroupImpl(final String name, final User owner, final String description, final Date creationDate) {
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public Kind getKind() {
        return Kind.GROUP;
    }

    public UserString getName() {
        return UserStringImpl.valueOf(name);
    }

    public User getOwner() {
        return owner;
    }

    public UserString getDescription() {
        return UserStringImpl.valueOf(description);
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Collection<? extends User> getMembers() {
        return members;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
