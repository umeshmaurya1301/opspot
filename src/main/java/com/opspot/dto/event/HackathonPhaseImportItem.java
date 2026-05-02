package com.opspot.dto.event;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class HackathonPhaseImportItem {
    private String phaseName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
    private Integer phaseOrder;
}
