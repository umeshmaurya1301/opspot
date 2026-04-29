package com.opspot.controller;

import com.opspot.dto.ai.AIToolOfferFilterParams;
import com.opspot.dto.ai.AIToolOfferResponseDTO;
import com.opspot.dto.common.ImportRequest;
import com.opspot.dto.common.ImportSummary;
import com.opspot.dto.common.StatusUpdateRequest;
import com.opspot.service.AIToolOfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai-offers")
@RequiredArgsConstructor
public class AIToolOfferController {

    private final AIToolOfferService aiToolOfferService;

    @PostMapping("/import")
    public ResponseEntity<ImportSummary> importAIToolOffers(@Valid @RequestBody ImportRequest request) {
        return ResponseEntity.ok(aiToolOfferService.importFromJson(request.getJson()));
    }

    @GetMapping
    public ResponseEntity<List<AIToolOfferResponseDTO>> getAIToolOffers(@ModelAttribute AIToolOfferFilterParams params) {
        return ResponseEntity.ok(aiToolOfferService.getAIToolOffers(params));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AIToolOfferResponseDTO> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(aiToolOfferService.updateStatus(id, request.getStatus()));
    }
}
