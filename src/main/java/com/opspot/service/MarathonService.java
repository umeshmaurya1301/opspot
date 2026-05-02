package com.opspot.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opspot.dto.common.ImportSummary;
import com.opspot.dto.marathon.MarathonFilterParams;
import com.opspot.dto.marathon.MarathonImportItem;
import com.opspot.dto.marathon.MarathonResponseDTO;
import com.opspot.entity.Marathon;
import com.opspot.enums.ApplicationStatus;
import com.opspot.exception.InvalidJsonException;
import com.opspot.exception.ResourceNotFoundException;
import com.opspot.repository.MarathonRepository;
import com.opspot.specification.MarathonSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MarathonService {

    private final MarathonRepository marathonRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public ImportSummary importFromJson(String json) {
        List<MarathonImportItem> items;
        try {
            JsonNode root = objectMapper.readTree(json);
            items = objectMapper.convertValue(root.get("items"),
                    new TypeReference<List<MarathonImportItem>>() {});
        } catch (Exception e) {
            log.error("Failed to parse marathons JSON: {}", e.getMessage());
            throw new InvalidJsonException("Invalid JSON format: " + e.getMessage());
        }

        if (items == null || items.isEmpty()) {
            return new ImportSummary(0, 0, 0);
        }

        int inserted = 0;
        int skipped = 0;

        for (MarathonImportItem item : items) {
            if (item.getRegistrationLink() == null || item.getRegistrationLink().isBlank()) {
                log.warn("Skipping marathon with missing registrationLink: {}", item.getTitle());
                skipped++;
                continue;
            }
            if (marathonRepository.existsByRegistrationLink(item.getRegistrationLink())) {
                log.warn("Skipping duplicate marathon: {}", item.getRegistrationLink());
                skipped++;
                continue;
            }
            Marathon marathon = Marathon.builder()
                    .title(item.getTitle())
                    .description(item.getDescription())
                    .organizer(item.getOrganizer())
                    .date(item.getDate())
                    .registrationDeadline(item.getRegistrationDeadline())
                    .entryFee(item.getEntryFee())
                    .isFree(Boolean.TRUE.equals(item.getIsFree()))
                    .location(item.getLocation())
                    .distanceCategories(item.getDistanceCategories() != null ? item.getDistanceCategories() : new java.util.ArrayList<>())
                    .registrationLink(item.getRegistrationLink())
                    .build();
            marathonRepository.save(marathon);
            inserted++;
            log.info("Imported marathon: {}", marathon.getTitle());
        }

        log.info("Marathons import complete — total: {}, inserted: {}, skipped: {}", items.size(), inserted, skipped);
        return new ImportSummary(items.size(), inserted, skipped);
    }

    public List<MarathonResponseDTO> getMarathons(MarathonFilterParams params) {
        return marathonRepository.findAll(MarathonSpecification.withFilters(params))
                .stream()
                .map(MarathonResponseDTO::new)
                .toList();
    }

    @Transactional
    public MarathonResponseDTO updateStatus(Long id, ApplicationStatus status) {
        Marathon marathon = marathonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marathon not found with id: " + id));
        marathon.setStatus(status);
        marathonRepository.save(marathon);
        log.info("Updated marathon {} status to {}", id, status);
        return new MarathonResponseDTO(marathon);
    }
}
