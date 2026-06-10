package com.calendar.application.dto;

import java.time.LocalDateTime;
import java.util.List;

public record EventResponse(
        String id,
        String title,
        String description,
        LocalDateTime dueDate,
        List<String> recipientEmails,
        boolean notified
) {}