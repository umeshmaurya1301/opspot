package com.opspot.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opspot.dto.common.ImportSummary;
import com.opspot.dto.job.JobFilterParams;
import com.opspot.dto.job.JobImportItem;
import com.opspot.dto.job.JobResponseDTO;
import com.opspot.entity.Job;
import com.opspot.enums.ApplicationStatus;
import com.opspot.exception.InvalidJsonException;
import com.opspot.exception.ResourceNotFoundException;
import com.opspot.repository.JobRepository;
import com.opspot.specification.JobSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobService {

    private final JobRepository jobRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public ImportSummary importFromJson(String json) {
        List<JobImportItem> items;
        try {
            JsonNode root = objectMapper.readTree(json);
            items = objectMapper.convertValue(root.get("items"),
                    new TypeReference<List<JobImportItem>>() {});
        } catch (Exception e) {
            log.error("Failed to parse jobs JSON: {}", e.getMessage());
            throw new InvalidJsonException("Invalid JSON format: " + e.getMessage());
        }

        if (items == null || items.isEmpty()) {
            return new ImportSummary(0, 0, 0);
        }

        int inserted = 0;
        int skipped = 0;

        for (JobImportItem item : items) {
            if (item.getJobLink() == null || item.getJobLink().isBlank()) {
                log.warn("Skipping job with missing jobLink: {}", item.getTitle());
                skipped++;
                continue;
            }
            if (jobRepository.existsByJobLink(item.getJobLink())) {
                log.warn("Skipping duplicate job: {}", item.getJobLink());
                skipped++;
                continue;
            }
            Job job = Job.builder()
                    .title(item.getTitle())
                    .company(item.getCompany())
                    .location(item.getLocation())
                    .workMode(item.getWorkMode())
                    .skills(item.getSkills())
                    .experienceMin(item.getExperienceMin())
                    .experienceMax(item.getExperienceMax())
                    .salaryRange(item.getSalaryRange())
                    .description(item.getDescription())
                    .jobLink(item.getJobLink())
                    .build();
            jobRepository.save(job);
            inserted++;
            log.info("Imported job: {} at {}", job.getTitle(), job.getCompany());
        }

        log.info("Jobs import complete — total: {}, inserted: {}, skipped: {}", items.size(), inserted, skipped);
        return new ImportSummary(items.size(), inserted, skipped);
    }

    public List<JobResponseDTO> getJobs(JobFilterParams params) {
        return jobRepository.findAll(JobSpecification.withFilters(params))
                .stream()
                .map(JobResponseDTO::new)
                .toList();
    }

    @Transactional
    public JobResponseDTO updateStatus(Long id, ApplicationStatus status) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));
        job.setStatus(status);
        jobRepository.save(job);
        log.info("Updated job {} status to {}", id, status);
        return new JobResponseDTO(job);
    }
}
