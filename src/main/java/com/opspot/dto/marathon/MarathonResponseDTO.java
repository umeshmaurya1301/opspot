package com.opspot.dto.marathon;

import com.opspot.entity.Marathon;
import com.opspot.enums.ApplicationStatus;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class MarathonResponseDTO {
    private final Long id;
    private final String title;
    private final String description;
    private final String organizer;
    private final LocalDate date;
    private final LocalDate registrationDeadline;
    private final Double entryFee;
    private final boolean isFree;
    private final String location;
    private final List<String> distanceCategories;
    private final String registrationLink;
    private final ApplicationStatus status;
    private final LocalDateTime createdAt;

    public MarathonResponseDTO(Marathon marathon) {
        this.id = marathon.getId();
        this.title = marathon.getTitle();
        this.description = marathon.getDescription();
        this.organizer = marathon.getOrganizer();
        this.date = marathon.getDate();
        this.registrationDeadline = marathon.getRegistrationDeadline();
        this.entryFee = marathon.getEntryFee();
        this.isFree = marathon.isFree();
        this.location = marathon.getLocation();
        this.distanceCategories = marathon.getDistanceCategories();
        this.registrationLink = marathon.getRegistrationLink();
        this.status = marathon.getStatus();
        this.createdAt = marathon.getCreatedAt();
    }
}
