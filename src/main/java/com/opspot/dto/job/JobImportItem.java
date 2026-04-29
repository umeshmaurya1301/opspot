package com.opspot.dto.job;

import com.opspot.enums.WorkMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JobImportItem {
    private String title;
    private String company;
    private String location;
    private WorkMode workMode;
    private String skills;
    private Integer experienceMin;
    private Integer experienceMax;
    private String salaryRange;
    private String description;
    private String jobLink;
}
