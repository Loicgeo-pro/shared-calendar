package com.calendar.domain.model;

import java.util.List;
import java.util.Objects;

public record Recipients(List<Recipient> values) {

    public Recipients {
        Objects.requireNonNull(values, "Recipients list must not be null");
        if (values.isEmpty()) {
            throw new IllegalArgumentException("At least one recipient is required");
        }
        values = List.copyOf(values);
    }

    public int count() {
        return values.size();
    }

    public List<String> emails() {
        return values.stream()
                .map(Recipient::email)
                .toList();
    }
}