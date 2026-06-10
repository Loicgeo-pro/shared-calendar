package com.calendar.domain.port;

import com.calendar.domain.model.Event;

public interface NotificationSender {

    void send(Event event);
}