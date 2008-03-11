package com.nraynaud.sport.hibernate;

import com.nraynaud.sport.*;
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

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "NIKEPLUSEMAIL")
    private String nikePluEmail;

    @Column(name = "NIKEPLUSPASSWORD")
    private String nikePlusPassword;

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

    public UserString getName() {
        return UserStringImpl.valueOf(name);
    }

    public UserString getTown() {
        return UserStringImpl.valueOf(town);
    }

    public UserString getDescription() {
        return UserStringImpl.valueOf(description);
    }

    public UserString getWebSite() {
        return UserStringImpl.valueOf(webSite);
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

    public String getBareName() {
        return name;
    }

    public UserString getEmail() {
        return decipher(email);
    }

    public void setEmail(final String email) {
        this.email = cipher(email);
    }

    public UserString getNikePluEmail() {
        return decipher(nikePluEmail);
    }

    public void setNikePluEmail(final String nikePluEmail) {
        this.nikePluEmail = cipher(nikePluEmail);
    }

    public UserString getNikePlusPassword() {
        return decipher(nikePlusPassword);
    }

    public void setNikePlusPassword(final String nikePlusPassword) {
        this.nikePlusPassword = cipher(nikePlusPassword);
    }

    private static UserString decipher(final String cleartext) {
        return cleartext != null ? UserStringImpl.valueOf(CipherHelper.decipher(cleartext)) : null;
    }

    private static String cipher(final String email) {
        return email != null ? CipherHelper.cipher(email) : null;
    }
}
