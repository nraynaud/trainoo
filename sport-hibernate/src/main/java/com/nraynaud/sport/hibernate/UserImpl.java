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

    public boolean checkPassword(final String candidate) {
        return BCrypt.checkpw(candidate, passwordHash);
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}
