package com.calendar.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class EventTest {

    private static final LocalDateTime FUTURE_DATE = LocalDateTime.of(2026, 12, 25, 10, 0);
    private static final Recipients RECIPIENTS = new Recipients(List.of(new Recipient("user@test.com")));

    @Nested
    @DisplayName("Event creation")
    class Creation {

        @Test
        @DisplayName("should create event with valid parameters")
        void should_create_event_with_valid_parameters() {
            // Given
            EventId identifier = EventId.generate();

            // When
            Event event = new Event(identifier, "Christmas", "Celebration", FUTURE_DATE, RECIPIENTS);

            // Then
            assertThat(event.identifier()).isEqualTo(identifier);
            assertThat(event.title()).isEqualTo("Christmas");
            assertThat(event.isNotified()).isFalse();
        }

        @Test
        @DisplayName("should reject null title")
        void should_reject_null_title() {
            // Given
            EventId identifier = EventId.generate();

            // When / Then
            assertThatNullPointerException()
                    .isThrownBy(() -> new Event(identifier, null, "desc", FUTURE_DATE, RECIPIENTS))
                    .withMessageContaining("Title");
        }
    }

    @Nested
    @DisplayName("Due date check")
    class DueDateCheck {

        @Test
        @DisplayName("should be due when current time is after due date")
        void should_be_due_when_current_time_is_after_due_date() {
            // Given
            LocalDateTime pastDate = LocalDateTime.of(2024, 1, 1, 0, 0);
            Event event = new Event(EventId.generate(), "Past", "desc", pastDate, RECIPIENTS);

            // When
            boolean isDue = event.isDue(LocalDateTime.now());

            // Then
            assertThat(isDue).isTrue();
        }

        @Test
        @DisplayName("should not be due when already notified")
        void should_not_be_due_when_already_notified() {
            // Given
            LocalDateTime pastDate = LocalDateTime.of(2024, 1, 1, 0, 0);
            Event event = new Event(EventId.generate(), "Past", "desc", pastDate, RECIPIENTS);
            event.markNotified();

            // When
            boolean isDue = event.isDue(LocalDateTime.now());

            // Then
            assertThat(isDue).isFalse();
        }

        @Test
        @DisplayName("should not be due when due date is in the future")
        void should_not_be_due_when_due_date_is_in_the_future() {
            // Given
            Event event = new Event(EventId.generate(), "Future", "desc", FUTURE_DATE, RECIPIENTS);

            // When
            boolean isDue = event.isDue(LocalDateTime.of(2025, 1, 1, 0, 0));

            // Then
            assertThat(isDue).isFalse();
        }
    }
}