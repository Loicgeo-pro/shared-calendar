package com.calendar.infrastructure.notification;

import com.calendar.domain.model.Event;
import com.calendar.domain.port.NotificationSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class EmailNotificationSender implements NotificationSender {

    private final JavaMailSender mailSender;

    @Override
    public void send(Event event) {
        SimpleMailMessage message = buildMessage(event);
        mailSender.send(message);
        log.info("Notification sent for event: {}", event.title());
    }

    private static SimpleMailMessage buildMessage(Event event) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.recipientEmails().toArray(String[]::new));
        message.setSubject("Reminder : " + event.title());
        message.setText(formatBody(event));
        return message;
    }

    private static String formatBody(Event event) {
        return """
                Hi,
                
                The event "%s" is coming up.
                
                Description : %s
                Scheduled Date : %s
                
                Best regards,
                The Calendar Manager
                """.formatted(
                event.title(),
                event.description(),
                event.dueDate()
        );
    }
}