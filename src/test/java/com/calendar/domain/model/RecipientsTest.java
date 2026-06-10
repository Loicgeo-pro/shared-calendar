package com.calendar.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class RecipientsTest {

    @Test
    @DisplayName("should create recipients with valid emails")
    void should_create_recipients_with_valid_emails() {
        // Given
        List<Recipient> recipientList = List.of(
                new Recipient("alice@test.com"),
                new Recipient("bob@test.com")
        );

        // When
        Recipients recipients = new Recipients(recipientList);

        // Then
        assertThat(recipients.count()).isEqualTo(2);
        assertThat(recipients.emails()).containsExactly("alice@test.com", "bob@test.com");
    }

    @Test
    @DisplayName("should reject empty recipient list")
    void should_reject_empty_recipient_list() {
        // Given
        List<Recipient> emptyList = List.of();

        // When / Then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Recipients(emptyList))
                .withMessageContaining("At least one recipient");
    }

    @Test
    @DisplayName("should reject invalid email format")
    void should_reject_invalid_email_format() {
        // When / Then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Recipient("invalid-email"))
                .withMessageContaining("Invalid email");
    }
}