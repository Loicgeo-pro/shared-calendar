package com.calendar.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.List;

public record UpdateEventRequest(
        @NotBlank String title,
        String description,
        LocalDateTime dueDate,
        @NotEmpty List<String> recipientEmails
) {}