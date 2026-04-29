package com.opspot.dto.ai;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AIToolOfferFilterParams {
    private String toolName;
    private Boolean isFree;
    private Boolean forProfessionals;
    private boolean showRejected = false;
}
