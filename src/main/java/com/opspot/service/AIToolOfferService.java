package com.opspot.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opspot.dto.ai.AIToolOfferFilterParams;
import com.opspot.dto.ai.AIToolOfferImportItem;
import com.opspot.dto.ai.AIToolOfferResponseDTO;
import com.opspot.dto.common.ImportSummary;
import com.opspot.entity.AIToolOffer;
import com.opspot.enums.ApplicationStatus;
import com.opspot.exception.InvalidJsonException;
import com.opspot.exception.ResourceNotFoundException;
import com.opspot.repository.AIToolOfferRepository;
import com.opspot.specification.AIToolOfferSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIToolOfferService {

    private final AIToolOfferRepository aiToolOfferRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public ImportSummary importFromJson(String json) {
        List<AIToolOfferImportItem> items;
        try {
            JsonNode root = objectMapper.readTree(json);
            items = objectMapper.convertValue(root.get("items"),
                    new TypeReference<List<AIToolOfferImportItem>>() {});
        } catch (Exception e) {
            log.error("Failed to parse AI tool offers JSON: {}", e.getMessage());
            throw new InvalidJsonException("Invalid JSON format: " + e.getMessage());
        }

        if (items == null || items.isEmpty()) {
            return new ImportSummary(0, 0, 0);
        }

        int inserted = 0;
        int skipped = 0;

        for (AIToolOfferImportItem item : items) {
            if (item.getOfferLink() == null || item.getOfferLink().isBlank()) {
                log.warn("Skipping AI tool offer with missing offerLink: {}", item.getOfferTitle());
                skipped++;
                continue;
            }
            if (aiToolOfferRepository.existsByOfferLink(item.getOfferLink())) {
                log.warn("Skipping duplicate AI tool offer: {}", item.getOfferLink());
                skipped++;
                continue;
            }
            AIToolOffer offer = AIToolOffer.builder()
                    .toolName(item.getToolName())
                    .offerTitle(item.getOfferTitle())
                    .isFree(Boolean.TRUE.equals(item.getIsFree()))
                    .forProfessionals(Boolean.TRUE.equals(item.getForProfessionals()))
                    .validTill(item.getValidTill())
                    .description(item.getDescription())
                    .offerLink(item.getOfferLink())
                    .build();
            aiToolOfferRepository.save(offer);
            inserted++;
            log.info("Imported AI tool offer: {} for {}", offer.getOfferTitle(), offer.getToolName());
        }

        log.info("AI tool offers import complete — total: {}, inserted: {}, skipped: {}", items.size(), inserted, skipped);
        return new ImportSummary(items.size(), inserted, skipped);
    }

    public List<AIToolOfferResponseDTO> getAIToolOffers(AIToolOfferFilterParams params) {
        return aiToolOfferRepository.findAll(AIToolOfferSpecification.withFilters(params))
                .stream()
                .map(AIToolOfferResponseDTO::new)
                .toList();
    }

    @Transactional
    public AIToolOfferResponseDTO updateStatus(Long id, ApplicationStatus status) {
        AIToolOffer offer = aiToolOfferRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AI tool offer not found with id: " + id));
        offer.setStatus(status);
        aiToolOfferRepository.save(offer);
        log.info("Updated AI tool offer {} status to {}", id, status);
        return new AIToolOfferResponseDTO(offer);
    }
}
