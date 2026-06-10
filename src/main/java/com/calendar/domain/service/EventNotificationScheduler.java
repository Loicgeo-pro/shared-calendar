package com.calendar.domain.service;

import com.calendar.domain.model.Event;
import com.calendar.domain.port.EventRepository;
import com.calendar.domain.port.NotificationSender;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class EventNotificationScheduler {

    private final EventRepository repository;
    private final NotificationSender notificationSender;
    private final Clock clock;

    public void checkAndNotify() {
        LocalDateTime now = LocalDateTime.now(clock);
        List<Event> dueEvents = repository.findAllNotNotified()
                .stream()
                .filter(event -> event.isDue(now))
                .toList();

        dueEvents.forEach(this::notifyAndMark);
    }

    private void notifyAndMark(Event event) {
        notificationSender.send(event);
        event.markNotified();
        repository.save(event);
    }
}