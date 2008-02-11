package com.nraynaud.sport.hibernate;

import com.nraynaud.sport.User;
import com.nraynaud.sport.Workout;
import org.mindrot.bcrypt.BCrypt;

import javax.persistence.*;
import java.util.Collection;

@SuppressWarnings({"UnusedDeclaration", "NonFinalFieldReferenceInEquals", "NonFinalFieldReferencedInHashCode"})
@Entity
@Table(name = "USERS")
public class UserImpl implements User {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", unique = true, nullable = false)
    private String name;

    @Column(name = "HASH", nullable = false)
    private String passwordHash;

    @SuppressWarnings({"UnusedDeclaration"})
    @Column(name = "TOWN")
    private String town;

    @SuppressWarnings({"UnusedDeclaration"})
    @Column(name = "DESCRIPTION")
    private String description;

    @SuppressWarnings({"UnusedDeclaration"})
    @Column(name = "WEBSITE")
    private String webSite;

    @Column(name = "REMEMBER_ME")
    private String rememberToken;

    @ManyToMany(targetEntity = UserImpl.class, fetch = FetchType.LAZY)
    @JoinTable(name = "GROUP_USER", joinColumns = @JoinColumn(name = "USER_ID", nullable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "GROUP_ID", nullable = false, updatable = false))
    private Collection<UserImpl> groups;

    @OneToMany(targetEntity = WorkoutImpl.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private Collection<Workout> workouts;

    public UserImpl() {
    }

    public UserImpl(final String name, final String password) {
        this.name = name;
        setPassword(password);
    }

    public Long getId() {
        return id;
    }

    public Collection<Workout> getWorkouts() {
        return workouts;
    }

    public String getName() {
        return name;
    }

    public String getTown() {
        return town;
    }

    public String getDescription() {
        return description;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setTown(final String town) {
        this.town = town;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setWebSite(final String webSite) {
        this.webSite = webSite;
    }

    public boolean checkPassword(final String candidate) {
        return BCrypt.checkpw(candidate, passwordHash);
    }

    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof UserImpl)) return false;
        final UserImpl user = (UserImpl) o;
        return !(id != null ? !id.equals(user.id) : user.id != null);
    }

    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public void setPassword(final String password) {
        passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public String getRememberToken() {
        return rememberToken;
    }

    public void setRememberToken(final String token) {
        rememberToken = token == null ? null : token.substring(0, 255);
    }

    public Collection<UserImpl> getGroups() {
        return groups;
    }
}
