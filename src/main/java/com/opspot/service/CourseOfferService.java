package com.opspot.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opspot.dto.common.ImportSummary;
import com.opspot.dto.course.CourseOfferFilterParams;
import com.opspot.dto.course.CourseOfferImportItem;
import com.opspot.dto.course.CourseOfferResponseDTO;
import com.opspot.entity.CourseOffer;
import com.opspot.enums.ApplicationStatus;
import com.opspot.exception.InvalidJsonException;
import com.opspot.exception.ResourceNotFoundException;
import com.opspot.repository.CourseOfferRepository;
import com.opspot.specification.CourseOfferSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseOfferService {

    private final CourseOfferRepository courseOfferRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public ImportSummary importFromJson(String json) {
        List<CourseOfferImportItem> items;
        try {
            JsonNode root = objectMapper.readTree(json);
            items = objectMapper.convertValue(root.get("items"),
                    new TypeReference<List<CourseOfferImportItem>>() {});
        } catch (Exception e) {
            log.error("Failed to parse course offers JSON: {}", e.getMessage());
            throw new InvalidJsonException("Invalid JSON format: " + e.getMessage());
        }

        if (items == null || items.isEmpty()) {
            return new ImportSummary(0, 0, 0);
        }

        int inserted = 0;
        int skipped = 0;

        for (CourseOfferImportItem item : items) {
            if (item.getOfferLink() == null || item.getOfferLink().isBlank()) {
                log.warn("Skipping course offer with missing offerLink: {}", item.getTitle());
                skipped++;
                continue;
            }
            if (courseOfferRepository.existsByOfferLink(item.getOfferLink())) {
                log.warn("Skipping duplicate course offer: {}", item.getOfferLink());
                skipped++;
                continue;
            }
            CourseOffer offer = CourseOffer.builder()
                    .title(item.getTitle())
                    .platform(item.getPlatform())
                    .topic(item.getTopic())
                    .originalPrice(item.getOriginalPrice())
                    .discountedPrice(item.getDiscountedPrice())
                    .isFree(Boolean.TRUE.equals(item.getIsFree()))
                    .validTill(item.getValidTill())
                    .description(item.getDescription())
                    .offerLink(item.getOfferLink())
                    .build();
            courseOfferRepository.save(offer);
            inserted++;
            log.info("Imported course offer: {} on {}", offer.getTitle(), offer.getPlatform());
        }

        log.info("Course offers import complete — total: {}, inserted: {}, skipped: {}", items.size(), inserted, skipped);
        return new ImportSummary(items.size(), inserted, skipped);
    }

    public List<CourseOfferResponseDTO> getCourseOffers(CourseOfferFilterParams params) {
        return courseOfferRepository.findAll(CourseOfferSpecification.withFilters(params))
                .stream()
                .map(CourseOfferResponseDTO::new)
                .toList();
    }

    @Transactional
    public CourseOfferResponseDTO updateStatus(Long id, ApplicationStatus status) {
        CourseOffer offer = courseOfferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course offer not found with id: " + id));
        offer.setStatus(status);
        courseOfferRepository.save(offer);
        log.info("Updated course offer {} status to {}", id, status);
        return new CourseOfferResponseDTO(offer);
    }
}
