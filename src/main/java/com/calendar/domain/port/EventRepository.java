package com.calendar.domain.port;

import com.calendar.domain.model.Event;
import com.calendar.domain.model.EventId;

import java.util.List;
import java.util.Optional;

public interface EventRepository {

    Event save(Event event);

    Optional<Event> findById(EventId identifier);

    List<Event> findAll();

    List<Event> findAllNotNotified();

    void deleteById(EventId identifier);

    boolean existsById(EventId identifier);
}