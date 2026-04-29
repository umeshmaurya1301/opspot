package com.opspot.dto.course;

import com.opspot.entity.CourseOffer;
import com.opspot.enums.ApplicationStatus;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class CourseOfferResponseDTO {
    private final Long id;
    private final String title;
    private final String platform;
    private final String topic;
    private final BigDecimal originalPrice;
    private final BigDecimal discountedPrice;
    private final Boolean isFree;
    private final LocalDate validTill;
    private final String description;
    private final String offerLink;
    private final ApplicationStatus status;
    private final LocalDateTime createdAt;

    public CourseOfferResponseDTO(CourseOffer offer) {
        this.id = offer.getId();
        this.title = offer.getTitle();
        this.platform = offer.getPlatform();
        this.topic = offer.getTopic();
        this.originalPrice = offer.getOriginalPrice();
        this.discountedPrice = offer.getDiscountedPrice();
        this.isFree = offer.getIsFree();
        this.validTill = offer.getValidTill();
        this.description = offer.getDescription();
        this.offerLink = offer.getOfferLink();
        this.status = offer.getStatus();
        this.createdAt = offer.getCreatedAt();
    }
}
