package com.opspot.dto.event;

import com.opspot.entity.Event;
import com.opspot.enums.ApplicationStatus;
import com.opspot.enums.EventType;
import com.opspot.enums.WorkMode;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class EventResponseDTO {
    private final Long id;
    private final String title;
    private final String description;
    private final String organizer;
    private final EventType eventType;
    private final WorkMode workMode;
    private final String city;
    private final String theme;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final LocalDate registrationDeadline;
    private final boolean professionalAllowed;
    private final String registrationLink;
    private final ApplicationStatus status;
    private final LocalDateTime createdAt;

    public EventResponseDTO(Event event) {
        this.id = event.getId();
        this.title = event.getTitle();
        this.description = event.getDescription();
        this.organizer = event.getOrganizer();
        this.eventType = event.getEventType();
        this.workMode = event.getWorkMode();
        this.city = event.getCity();
        this.theme = event.getTheme();
        this.startDate = event.getStartDate();
        this.endDate = event.getEndDate();
        this.registrationDeadline = event.getRegistrationDeadline();
        this.professionalAllowed = event.isProfessionalAllowed();
        this.registrationLink = event.getRegistrationLink();
        this.status = event.getStatus();
        this.createdAt = event.getCreatedAt();
    }
}
