package com.opspot.dto.event;

import com.opspot.entity.HackathonPhase;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class HackathonPhaseResponseDTO {
    private final Long id;
    private final String phaseName;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String location;
    private final Integer phaseOrder;

    public HackathonPhaseResponseDTO(HackathonPhase phase) {
        this.id = phase.getId();
        this.phaseName = phase.getPhaseName();
        this.startDate = phase.getStartDate();
        this.endDate = phase.getEndDate();
        this.location = phase.getLocation();
        this.phaseOrder = phase.getPhaseOrder();
    }
}
