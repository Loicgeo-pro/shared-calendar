package com.calendar.configuration;

import com.calendar.domain.port.EventRepository;
import com.calendar.domain.port.NotificationSender;
import com.calendar.domain.service.EventNotificationScheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Clock;

@Configuration
public class MailSenderConfiguration {

    @Bean
    public EventNotificationScheduler notificationScheduler(EventRepository repository,
                                                            NotificationSender sender,
                                                            Clock clock) {
        return new EventNotificationScheduler(repository, sender, clock);
    }

    @Bean
    public Runnable scheduledNotificationTask(EventNotificationScheduler scheduler) {
        return new Runnable() {
            @Scheduled(fixedRateString = "${notification.check.interval:60000}")
            @Override
            public void run() {
                scheduler.checkAndNotify();
            }
        };
    }
}
