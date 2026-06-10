package com.calendar.application.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.List;

public record CreateEventRequest(
        @NotBlank String title,
        String description,
        @Future LocalDateTime dueDate,
        @NotEmpty List<String> recipientEmails
) {}