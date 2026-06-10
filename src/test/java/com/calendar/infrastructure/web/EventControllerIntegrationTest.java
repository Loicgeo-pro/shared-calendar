package com.calendar.infrastructure.web;

import com.calendar.application.dto.CreateEventRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EventControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateEventRequest sampleRequest() {
        return new CreateEventRequest(
                "Sprint Review",
                "Démo de fin de sprint",
                LocalDateTime.of(2027, 1, 15, 14, 0),
                List.of("team@company.com")
        );
    }

    @Test
    @DisplayName("should create event and return 201")
    void should_create_event_and_return_201() throws Exception {
        // Given
        CreateEventRequest request = sampleRequest();
        String json = objectMapper.writeValueAsString(request);

        // When
        MvcResult result = mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // Then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Sprint Review"))
                .andExpect(jsonPath("$.notified").value(false))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).contains("Sprint Review");
    }

    @Test
    @DisplayName("should list all events")
    void should_list_all_events() throws Exception {
        // Given — create one event first
        String json = objectMapper.writeValueAsString(sampleRequest());
        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        // When / Then
        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("should return 404 for unknown event")
    void should_return_404_for_unknown_event() throws Exception {
        // Given
        String unknownId = "00000000-0000-0000-0000-000000000000";

        // When / Then
        mockMvc.perform(get("/api/events/" + unknownId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("should delete event and return 204")
    void should_delete_event_and_return_204() throws Exception {
        // Given — create event
        String json = objectMapper.writeValueAsString(sampleRequest());
        MvcResult createResult = mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        String id = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .get("id").asText();

        // When / Then
        mockMvc.perform(delete("/api/events/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/events/" + id))
                .andExpect(status().isNotFound());
    }
}