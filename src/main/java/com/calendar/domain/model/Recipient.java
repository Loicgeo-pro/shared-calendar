package com.calendar.domain.model;

import java.util.Objects;

public record Recipient(String email) {

    public Recipient {
        Objects.requireNonNull(email, "Email must not be null");
        if (!email.matches("^[\\w.+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Invalid email: " + email);
        }
    }
}