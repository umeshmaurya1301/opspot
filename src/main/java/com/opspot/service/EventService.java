package com.opspot.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.opspot.dto.common.ImportSummary;
import com.opspot.dto.event.EventFilterParams;
import com.opspot.dto.event.EventImportItem;
import com.opspot.dto.event.EventResponseDTO;
import com.opspot.entity.Event;
import com.opspot.enums.ApplicationStatus;
import com.opspot.exception.InvalidJsonException;
import com.opspot.exception.ResourceNotFoundException;
import com.opspot.repository.EventRepository;
import com.opspot.specification.EventSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final EventRepository eventRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public ImportSummary importFromJson(String json) {
        List<EventImportItem> items;
        try {
            JsonNode root = objectMapper.readTree(json);
            items = objectMapper.convertValue(root.get("items"),
                    new TypeReference<List<EventImportItem>>() {});
        } catch (Exception e) {
            log.error("Failed to parse events JSON: {}", e.getMessage());
            throw new InvalidJsonException("Invalid JSON format: " + e.getMessage());
        }

        if (items == null || items.isEmpty()) {
            return new ImportSummary(0, 0, 0);
        }

        int inserted = 0;
        int skipped = 0;

        for (EventImportItem item : items) {
            if (item.getRegistrationLink() == null || item.getRegistrationLink().isBlank()) {
                log.warn("Skipping event with missing registrationLink: {}", item.getTitle());
                skipped++;
                continue;
            }
            if (eventRepository.existsByRegistrationLink(item.getRegistrationLink())) {
                log.warn("Skipping duplicate event: {}", item.getRegistrationLink());
                skipped++;
                continue;
            }
            Event event = Event.builder()
                    .title(item.getTitle())
                    .description(item.getDescription())
                    .organizer(item.getOrganizer())
                    .eventType(item.getEventType())
                    .workMode(item.getWorkMode())
                    .city(item.getCity())
                    .theme(item.getTheme())
                    .startDate(item.getStartDate())
                    .endDate(item.getEndDate())
                    .registrationDeadline(item.getRegistrationDeadline())
                    .professionalAllowed(item.isProfessionalAllowed())
                    .registrationLink(item.getRegistrationLink())
                    .build();
            eventRepository.save(event);
            inserted++;
            log.info("Imported event: {}", event.getTitle());
        }

        log.info("Events import complete — total: {}, inserted: {}, skipped: {}", items.size(), inserted, skipped);
        return new ImportSummary(items.size(), inserted, skipped);
    }

    public List<EventResponseDTO> getEvents(EventFilterParams params) {
        return eventRepository.findAll(EventSpecification.withFilters(params))
                .stream()
                .map(EventResponseDTO::new)
                .toList();
    }

    @Transactional
    public EventResponseDTO updateStatus(Long id, ApplicationStatus status) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        event.setStatus(status);
        eventRepository.save(event);
        log.info("Updated event {} status to {}", id, status);
        return new EventResponseDTO(event);
    }
}
