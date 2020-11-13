package com.steven.willysbakeshop.model;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "verification_tokens", schema = "public")
public class VerificationToken {
    private static final int EXPIRY = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "expiration")
    private Date exipryDate;

    public VerificationToken() {
    }

    public VerificationToken(String token) {
        this.token = token;
        this.exipryDate = calculateExpiryDate(EXPIRY);
    }

    public VerificationToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.exipryDate = calculateExpiryDate(EXPIRY);
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getExipryDate() {
        return exipryDate;
    }

    public void setExipryDate(Date exipryDate) {
        this.exipryDate = exipryDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VerificationToken)) return false;
        VerificationToken that = (VerificationToken) o;
        return Objects.equals(getToken(), that.getToken()) &&
                Objects.equals(getUser(), that.getUser()) &&
                Objects.equals(getExipryDate(), that.getExipryDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getToken(), getUser(), getExipryDate());
    }

    @Override
    public String toString() {
        return "VerificationToken{" +
                "token='" + token + '\'' +
                ", exipryDate=" + exipryDate +
                '}';
    }

    private Date calculateExpiryDate(int timeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, timeInMinutes);

        return new Date(cal.getTime().getTime());
    }
}
