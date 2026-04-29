package com.opspot.dto.ai;

import com.opspot.entity.AIToolOffer;
import com.opspot.enums.ApplicationStatus;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class AIToolOfferResponseDTO {
    private final Long id;
    private final String toolName;
    private final String offerTitle;
    private final Boolean isFree;
    private final Boolean forProfessionals;
    private final LocalDate validTill;
    private final String description;
    private final String offerLink;
    private final ApplicationStatus status;
    private final LocalDateTime createdAt;

    public AIToolOfferResponseDTO(AIToolOffer offer) {
        this.id = offer.getId();
        this.toolName = offer.getToolName();
        this.offerTitle = offer.getOfferTitle();
        this.isFree = offer.getIsFree();
        this.forProfessionals = offer.getForProfessionals();
        this.validTill = offer.getValidTill();
        this.description = offer.getDescription();
        this.offerLink = offer.getOfferLink();
        this.status = offer.getStatus();
        this.createdAt = offer.getCreatedAt();
    }
}
