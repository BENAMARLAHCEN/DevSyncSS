package com.example.devsyncss.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "modification_tokens")
    private int modificationTokens;

    @Column(name = "deletion_tokens")
    private int deletionTokens;

    @Column(name = "last_reset_date")
    private LocalDateTime lastResetDate;

    public Token() {
    }

    public Token(User user, int modificationTokens, int deletionTokens, LocalDateTime lastResetDate) {
        this.user = user;
        this.modificationTokens = modificationTokens;
        this.deletionTokens = deletionTokens;
        this.lastResetDate = lastResetDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getModificationTokens() {
        return modificationTokens;
    }

    public void setModificationTokens(int modificationTokens) {
        this.modificationTokens = modificationTokens;
    }

    public int getDeletionTokens() {
        return deletionTokens;
    }

    public void setDeletionTokens(int deletionTokens) {
        this.deletionTokens = deletionTokens;
    }

    public LocalDateTime getLastResetDate() {
        return lastResetDate;
    }

    public void setLastResetDate(LocalDateTime lastResetDate) {
        this.lastResetDate = lastResetDate;
    }
}