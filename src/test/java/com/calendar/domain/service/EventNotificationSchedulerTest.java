package com.calendar.domain.service;

import com.calendar.domain.model.*;
import com.calendar.domain.port.EventRepository;
import com.calendar.domain.port.NotificationSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class EventNotificationSchedulerTest {

    @Mock
    private EventRepository repository;

    @Mock
    private NotificationSender notificationSender;

    private EventNotificationScheduler scheduler;

    private static final LocalDateTime NOW = LocalDateTime.of(2026, 6, 15, 12, 0);
    private static final Clock FIXED_CLOCK = Clock.fixed(
            NOW.toInstant(ZoneOffset.UTC),
            ZoneId.of("UTC")
    );
    private static final Recipients RECIPIENTS = new Recipients(
            List.of(new Recipient("notify@mail.com"))
    );

    @BeforeEach
    void setUp() {
        scheduler = new EventNotificationScheduler(repository, notificationSender, FIXED_CLOCK);
    }

    @Test
    @DisplayName("should send notification for due events")
    void should_send_notification_for_due_events() {
        // Given
        Event dueEvent = new Event(
                EventId.generate(), "Échéance passée", "desc",
                NOW.minusDays(1), RECIPIENTS
        );
        given(repository.findAllNotNotified()).willReturn(List.of(dueEvent));

        // When
        scheduler.checkAndNotify();

        // Then
        then(notificationSender).should().send(dueEvent);
        then(repository).should().save(dueEvent);
        assertThat(dueEvent.isNotified()).isTrue();
    }

    @Test
    @DisplayName("should not send notification for future events")
    void should_not_send_notification_for_future_events() {
        // Given
        Event futureEvent = new Event(
                EventId.generate(), "Futur", "desc",
                NOW.plusDays(5), RECIPIENTS
        );
        given(repository.findAllNotNotified()).willReturn(List.of(futureEvent));

        // When
        scheduler.checkAndNotify();

        // Then
        then(notificationSender).should(never()).send(futureEvent);
        assertThat(futureEvent.isNotified()).isFalse();
    }

    @Test
    @DisplayName("should handle mixed due and future events")
    void should_handle_mixed_due_and_future_events() {
        // Given
        Event dueEvent = new Event(
                EventId.generate(), "Due", "desc",
                NOW.minusHours(1), RECIPIENTS
        );
        Event futureEvent = new Event(
                EventId.generate(), "Future", "desc",
                NOW.plusDays(10), RECIPIENTS
        );
        given(repository.findAllNotNotified()).willReturn(List.of(dueEvent, futureEvent));

        // When
        scheduler.checkAndNotify();

        // Then
        then(notificationSender).should().send(dueEvent);
        then(notificationSender).should(never()).send(futureEvent);
    }
}