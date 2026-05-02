package com.opspot.dto.event;

import com.opspot.entity.Event;
import com.opspot.enums.ApplicationStatus;
import com.opspot.enums.EventType;
import com.opspot.enums.WorkMode;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class HackathonEventDetailsResponseDTO {
    private final Long id;
    private final String title;
    private final String description;
    private final String organizer;
    private final EventType eventType;
    private final WorkMode workMode;
    private final String city;
    private final List<String> themes;
    private final List<String> problemStatements;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final LocalDate registrationDeadline;
    private final boolean studentAllowed;
    private final boolean professionalAllowed;
    private final Integer minTeamSize;
    private final Integer maxTeamSize;
    private final String registrationLink;
    private final ApplicationStatus status;
    private final LocalDateTime createdAt;
    private final List<HackathonPhaseResponseDTO> phases;

    public HackathonEventDetailsResponseDTO(Event event) {
        this.id = event.getId();
        this.title = event.getTitle();
        this.description = event.getDescription();
        this.organizer = event.getOrganizer();
        this.eventType = event.getEventType();
        this.workMode = event.getWorkMode();
        this.city = event.getCity();
        this.themes = event.getThemes();
        this.problemStatements = event.getProblemStatements();
        this.startDate = event.getStartDate();
        this.endDate = event.getEndDate();
        this.registrationDeadline = event.getRegistrationDeadline();
        this.studentAllowed = event.isStudentAllowed();
        this.professionalAllowed = event.isProfessionalAllowed();
        this.minTeamSize = event.getMinTeamSize();
        this.maxTeamSize = event.getMaxTeamSize();
        this.registrationLink = event.getRegistrationLink();
        this.status = event.getStatus();
        this.createdAt = event.getCreatedAt();
        this.phases = event.getPhases().stream()
                .map(HackathonPhaseResponseDTO::new)
                .toList();
    }
}
