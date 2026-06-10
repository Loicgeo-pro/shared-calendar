package com.calendar.domain.service;

import com.calendar.domain.model.Event;
import com.calendar.domain.model.EventId;
import com.calendar.domain.port.EventRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class EventService {

    private final EventRepository repository;

    public Event create(Event event) {
        return repository.save(event);
    }

    public Event findById(EventId identifier) {
        return repository.findById(identifier)
                .orElseThrow(() -> new EventNotFoundException(identifier));
    }

    public List<Event> findAll() {
        return repository.findAll();
    }

    public Event update(EventId identifier, Event updatedEvent) {
        Event existing = findById(identifier);
        existing.update(
                updatedEvent.title(),
                updatedEvent.description(),
                updatedEvent.dueDate(),
                updatedEvent.recipients()
        );
        return repository.save(existing);
    }

    public void delete(EventId identifier) {
        validateExistence(identifier);
        repository.deleteById(identifier);
    }

    private void validateExistence(EventId identifier) {
        if (!repository.existsById(identifier)) {
            throw new EventNotFoundException(identifier);
        }
    }

    public static class EventNotFoundException extends RuntimeException {
        public EventNotFoundException(EventId identifier) {
            super("Event not found: " + identifier.value());
        }
    }
}