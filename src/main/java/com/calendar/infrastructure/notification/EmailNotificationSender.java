package com.calendar.infrastructure.notification;

import com.calendar.domain.model.Event;
import com.calendar.domain.port.NotificationSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationSender implements NotificationSender {

    private static final Logger log = LoggerFactory.getLogger(EmailNotificationSender.class);

    private final JavaMailSender mailSender;

    public EmailNotificationSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void send(Event event) {
        SimpleMailMessage message = buildMessage(event);
        mailSender.send(message);
        log.info("Notification sent for event: {}", event.title());
    }

    private SimpleMailMessage buildMessage(Event event) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.recipients().emails().toArray(String[]::new));
        message.setSubject("Rappel : " + event.title());
        message.setText(formatBody(event));
        return message;
    }

    private String formatBody(Event event) {
        return """
                Bonjour,
                
                L'événement "%s" arrive à échéance.
                
                Description : %s
                Date prévue : %s
                
                Cordialement,
                Le Gestionnaire de Calendrier
                """.formatted(
                event.title(),
                event.description(),
                event.dueDate()
        );
    }
}