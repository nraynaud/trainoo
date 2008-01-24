package com.nraynaud.sport.hibernate;

import com.nraynaud.sport.User;
import org.mindrot.bcrypt.BCrypt;

import javax.persistence.*;

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

    public UserImpl() {
    }

    public UserImpl(final String name, final String password) {
        this.name = name;
        this.passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public Long getId() {
        return id;
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
}
