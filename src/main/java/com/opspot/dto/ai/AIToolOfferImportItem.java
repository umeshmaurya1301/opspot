package com.opspot.dto.ai;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class AIToolOfferImportItem {
    private String toolName;
    private String offerTitle;
    private Boolean isFree;
    private Boolean forProfessionals;
    private LocalDate validTill;
    private String description;
    private String offerLink;
}
