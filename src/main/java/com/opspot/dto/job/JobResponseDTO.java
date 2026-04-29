package com.opspot.dto.job;

import com.opspot.entity.Job;
import com.opspot.enums.ApplicationStatus;
import com.opspot.enums.WorkMode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class JobResponseDTO {
    private final Long id;
    private final String title;
    private final String company;
    private final String location;
    private final WorkMode workMode;
    private final String skills;
    private final Integer experienceMin;
    private final Integer experienceMax;
    private final String salaryRange;
    private final String description;
    private final String jobLink;
    private final ApplicationStatus status;
    private final LocalDateTime createdAt;

    public JobResponseDTO(Job job) {
        this.id = job.getId();
        this.title = job.getTitle();
        this.company = job.getCompany();
        this.location = job.getLocation();
        this.workMode = job.getWorkMode();
        this.skills = job.getSkills();
        this.experienceMin = job.getExperienceMin();
        this.experienceMax = job.getExperienceMax();
        this.salaryRange = job.getSalaryRange();
        this.description = job.getDescription();
        this.jobLink = job.getJobLink();
        this.status = job.getStatus();
        this.createdAt = job.getCreatedAt();
    }
}
