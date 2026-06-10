package com.calendar.domain.service;

import com.calendar.domain.model.*;
import com.calendar.domain.port.EventRepository;
import com.calendar.domain.service.EventService.EventNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository repository;

    private EventService eventService;

    private static final Recipients RECIPIENTS = new Recipients(
            List.of(new Recipient("test@mail.com"))
    );

    @BeforeEach
    void setUp() {
        eventService = new EventService(repository);
    }

    private Event createSampleEvent() {
        return new Event(
                EventId.generate(),
                "Réunion",
                "Réunion hebdomadaire",
                LocalDateTime.of(2026, 7, 1, 9, 0),
                RECIPIENTS
        );
    }

    @Nested
    @DisplayName("Create event")
    class CreateEvent {

        @Test
        @DisplayName("should save and return the event")
        void should_save_and_return_event() {
            // Given
            Event event = createSampleEvent();
            given(repository.save(any(Event.class))).willReturn(event);

            // When
            Event result = eventService.create(event);

            // Then
            assertThat(result.title()).isEqualTo("Réunion");
            then(repository).should().save(event);
        }
    }

    @Nested
    @DisplayName("Find event by ID")
    class FindById {

        @Test
        @DisplayName("should return event when found")
        void should_return_event_when_found() {
            // Given
            Event event = createSampleEvent();
            given(repository.findById(event.identifier())).willReturn(Optional.of(event));

            // When
            Event result = eventService.findById(event.identifier());

            // Then
            assertThat(result.title()).isEqualTo("Réunion");
        }

        @Test
        @DisplayName("should throw when event not found")
        void should_throw_when_event_not_found() {
            // Given
            EventId unknownId = EventId.generate();
            given(repository.findById(unknownId)).willReturn(Optional.empty());

            // When / Then
            assertThatThrownBy(() -> eventService.findById(unknownId))
                    .isInstanceOf(EventNotFoundException.class)
                    .hasMessageContaining(unknownId.value().toString());
        }
    }

    @Nested
    @DisplayName("Delete event")
    class DeleteEvent {

        @Test
        @DisplayName("should delete existing event")
        void should_delete_existing_event() {
            // Given
            EventId identifier = EventId.generate();
            given(repository.existsById(identifier)).willReturn(true);

            // When
            eventService.delete(identifier);

            // Then
            then(repository).should().deleteById(identifier);
        }

        @Test
        @DisplayName("should throw when deleting non-existing event")
        void should_throw_when_deleting_non_existing_event() {
            // Given
            EventId unknownId = EventId.generate();
            given(repository.existsById(unknownId)).willReturn(false);

            // When / Then
            assertThatThrownBy(() -> eventService.delete(unknownId))
                    .isInstanceOf(EventNotFoundException.class);
        }
    }
}