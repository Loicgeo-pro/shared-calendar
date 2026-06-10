package com.calendar.configuration;

import com.calendar.domain.port.EventRepository;
import com.calendar.domain.service.EventService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public EventService eventService(EventRepository repository) {
        return new EventService(repository);
    }

}
