package com.helpdesk.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private TicketStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime lastModified;
    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @PrePersist
    public void onCreate() {
        LocalDateTime localDateTime = LocalDateTime.now();
        createdAt = localDateTime;
        lastModified = localDateTime;
    }

    @PreUpdate
    public void onUpdate() {
        lastModified = LocalDateTime.now();
    }

    protected Ticket() {
    }

    public Ticket(User createdBy, String title, String description) {
        this.title = title;
        this.description = description;
        this.status = TicketStatus.NEW;
        this.createdBy = createdBy;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public User getCreatedBy() {
        return createdBy;
    }
}
