package com.opspot.dto.event;

import com.opspot.enums.EventType;
import com.opspot.enums.WorkMode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class EventFilterParams {
    private String city;
    private EventType eventType;
    private WorkMode workMode;
    private String theme;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDateFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDateTo;

    private Boolean professionalAllowed;
    private boolean showRejected = false;
}
