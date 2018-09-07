package com.home.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "VERIFICATION_TOKENS")
public class VerificationToken {
    @Transient
    private int expiryTimeInMinutes = 60 * 24;

    @Id
    @TableGenerator(name = "tokenId",
            table="ID_GEN",
            pkColumnName = "GEN_NAME",
            valueColumnName = "GEN_VAL",
            pkColumnValue = "tokenId",
            allocationSize = 1)
    @GeneratedValue(generator = "tokenId")
    private int id;
    private String token;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "USER_ID")
    private User user;
    private LocalDateTime expiryDate;

    public VerificationToken() {}

    public VerificationToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.expiryDate = calculateExpiryDate();
    }

    private LocalDateTime calculateExpiryDate() {
        return LocalDateTime.now().plusMinutes(expiryTimeInMinutes);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getExpiryTimeInMinutes() {
        return expiryTimeInMinutes;
    }

    public void setExpiryTimeInMinutes(int expiryTimeInMinutes) {
        this.expiryTimeInMinutes = expiryTimeInMinutes;
    }

    @Override
    public String toString() {
        return "VerificationToken{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", user=" + user +
                ", expiryDate=" + expiryDate +
                '}';
    }
}
