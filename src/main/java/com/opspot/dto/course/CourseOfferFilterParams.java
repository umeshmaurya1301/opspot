package com.opspot.dto.course;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CourseOfferFilterParams {
    private String platform;
    private String topic;
    private BigDecimal maxPrice;
    private Boolean isFree;
    private boolean showRejected = false;
}
