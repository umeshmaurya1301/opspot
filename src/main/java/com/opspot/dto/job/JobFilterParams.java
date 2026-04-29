package com.opspot.dto.job;

import com.opspot.enums.WorkMode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobFilterParams {
    private String location;
    private WorkMode workMode;
    private String skills;
    private Integer experienceMin;
    private Integer experienceMax;
    private boolean showRejected = false;
}
