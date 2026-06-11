package com.calendar.domain.service;

import com.calendar.domain.model.Event;
import com.calendar.domain.port.EventRepository;
import com.calendar.domain.port.NotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class EventNotificationScheduler {

    private final EventRepository repository;
    private final NotificationSender notificationSender;
    private final Clock clock;

    public void checkAndNotify() {
        LocalDateTime now = LocalDateTime.now(clock);
        log.info("Checking events to be sent");
        List<Event> dueEvents = repository.findAllNotNotified()
                .stream()
                .filter(event -> event.isDue(now))
                .toList();

        log.info("Found {} events to be sent", dueEvents.size());
        dueEvents.forEach(this::notifyAndMark);
    }

    private void notifyAndMark(Event event) {
        notificationSender.send(event);
        event.markNotified();
        repository.save(event);
    }
}