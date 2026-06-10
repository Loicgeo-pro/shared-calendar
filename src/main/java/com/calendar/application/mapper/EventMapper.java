package com.calendar.application.mapper;

import com.calendar.application.dto.CreateEventRequest;
import com.calendar.application.dto.EventResponse;
import com.calendar.application.dto.UpdateEventRequest;
import com.calendar.domain.model.*;

public final class EventMapper {

    private EventMapper() {}

    public static Event toDomain(CreateEventRequest request) {
        Recipients recipients = toRecipients(request.recipientEmails());
        return new Event(
                EventId.generate(),
                request.title(),
                request.description(),
                request.dueDate(),
                recipients
        );
    }

    public static Event toDomain(EventId identifier, UpdateEventRequest request) {
        Recipients recipients = toRecipients(request.recipientEmails());
        return new Event(
                identifier,
                request.title(),
                request.description(),
                request.dueDate(),
                recipients
        );
    }

    public static EventResponse toResponse(Event event) {
        return new EventResponse(
                event.identifier().value().toString(),
                event.title(),
                event.description(),
                event.dueDate(),
                event.recipients().emails(),
                event.isNotified()
        );
    }

    private static Recipients toRecipients(java.util.List<String> emails) {
        return new Recipients(
                emails.stream()
                        .map(Recipient::new)
                        .toList()
        );
    }
}