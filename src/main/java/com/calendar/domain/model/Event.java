package com.calendar.domain.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

@EqualsAndHashCode
@ToString
public class Event {

    private final EventId identifier;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Recipients recipients;
    private boolean notified;

    public Event(EventId identifier,
                 String title,
                 String description,
                 LocalDateTime dueDate,
                 Recipients recipients) {
        this.identifier = Objects.requireNonNull(identifier);
        update(title, description, dueDate, recipients);
    }

    public void update(String title,
                       String description,
                       LocalDateTime dueDate,
                       Recipients recipients) {
        Objects.requireNonNull(title, "Title must not be null");
        Objects.requireNonNull(dueDate, "Due date must not be null");
        Objects.requireNonNull(recipients, "Recipients must not be null");
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.recipients = recipients;
        this.notified = false;
    }

    public boolean isDue(LocalDateTime now) {
        return !notified && !dueDate.isAfter(now);
    }

    public void markNotified() {
        this.notified = true;
    }

    public EventId identifier() {
        return identifier;
    }

    public String title() {
        return title;
    }

    public String description() {
        return description;
    }

    public LocalDateTime dueDate() {
        return dueDate;
    }

    public Recipients recipients() {
        return recipients;
    }

    public Collection<String> recipientEmails() {
        return recipients.emails();
    }

    public boolean isNotified() {
        return notified;
    }
}