package com.opspot.dto.event;

import com.opspot.enums.EventType;
import com.opspot.enums.WorkMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class EventImportItem {
    private String title;
    private String description;
    private String organizer;
    private EventType eventType;
    private WorkMode workMode;
    private String city;
    private String theme;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate registrationDeadline;
    private boolean professionalAllowed;
    private String registrationLink;
}
