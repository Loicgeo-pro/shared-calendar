package com.calendar.infrastructure.web;

import com.calendar.application.dto.*;
import com.calendar.application.mapper.EventMapper;
import com.calendar.domain.model.Event;
import com.calendar.domain.model.EventId;
import com.calendar.domain.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> findById(@PathVariable String id) {
        Event event = eventService.findById(EventId.from(id));
        return ResponseEntity.ok(EventMapper.toResponse(event));
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> findAll() {
        List<EventResponse> responses = eventService.findAll()
                .stream()
                .map(EventMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<EventResponse> create(@Valid @RequestBody CreateEventRequest request) {
        Event created = eventService.create(EventMapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(EventMapper.toResponse(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> update(@PathVariable String id,
                                                @Valid @RequestBody UpdateEventRequest request) {
        EventId identifier = EventId.from(id);
        Event updated = eventService.update(identifier, EventMapper.toDomain(identifier, request));
        return ResponseEntity.ok(EventMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        eventService.delete(EventId.from(id));
        return ResponseEntity.noContent().build();
    }
}