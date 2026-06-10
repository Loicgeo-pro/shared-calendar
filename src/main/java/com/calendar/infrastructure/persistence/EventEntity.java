package com.calendar.infrastructure.persistence;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "events")
public class EventEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private LocalDateTime dueDate;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "event_recipients", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "email")
    private List<String> recipientEmails;

    private boolean notified;

    protected EventEntity() {}

    public EventEntity(String id,
                       String title,
                       String description,
                       LocalDateTime dueDate,
                       List<String> recipientEmails,
                       boolean notified) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.recipientEmails = recipientEmails;
        this.notified = notified;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDateTime getDueDate() { return dueDate; }
    public List<String> getRecipientEmails() { return recipientEmails; }
    public boolean isNotified() { return notified; }
}