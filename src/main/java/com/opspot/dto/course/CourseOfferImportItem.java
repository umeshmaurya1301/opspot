package com.opspot.dto.course;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class CourseOfferImportItem {
    private String title;
    private String platform;
    private String topic;
    private BigDecimal originalPrice;
    private BigDecimal discountedPrice;
    private Boolean isFree;
    private LocalDate validTill;
    private String description;
    private String offerLink;
}
