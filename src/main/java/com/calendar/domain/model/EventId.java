package com.calendar.domain.model;

import java.util.Objects;
import java.util.UUID;

public record EventId(UUID value) {

    public EventId {
        Objects.requireNonNull(value, "EventId must not be null");
    }

    public static EventId generate() {
        return new EventId(UUID.randomUUID());
    }

    public static EventId from(String raw) {
        return new EventId(UUID.fromString(raw));
    }
}