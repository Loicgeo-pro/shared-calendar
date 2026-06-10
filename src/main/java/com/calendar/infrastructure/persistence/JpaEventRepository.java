package com.calendar.infrastructure.persistence;

import com.calendar.domain.model.*;
import com.calendar.domain.port.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaEventRepository implements EventRepository {

    private final SpringDataEventRepository springRepository;

    @Override
    public Event save(Event event) {
        EventEntity entity = toEntity(event);
        springRepository.save(entity);
        return event;
    }

    @Override
    public Optional<Event> findById(EventId identifier) {
        return springRepository.findById(identifier.value().toString())
                .map(this::toDomain);
    }

    @Override
    public List<Event> findAll() {
        return springRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Event> findAllNotNotified() {
        return springRepository.findByNotifiedFalse()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void deleteById(EventId identifier) {
        springRepository.deleteById(identifier.value().toString());
    }

    @Override
    public boolean existsById(EventId identifier) {
        return springRepository.existsById(identifier.value().toString());
    }

    private EventEntity toEntity(Event event) {
        return new EventEntity(
                event.identifier().value().toString(),
                event.title(),
                event.description(),
                event.dueDate(),
                event.recipients().emails(),
                event.isNotified()
        );
    }

    private Event toDomain(EventEntity entity) {
        Recipients recipients = new Recipients(
                entity.getRecipientEmails().stream()
                        .map(Recipient::new)
                        .toList()
        );
        Event event = new Event(
                EventId.from(entity.getId()),
                entity.getTitle(),
                entity.getDescription(),
                entity.getDueDate(),
                recipients
        );
        if (entity.isNotified()) {
            event.markNotified();
        }
        return event;
    }
}