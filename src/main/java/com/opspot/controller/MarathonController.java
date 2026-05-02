package com.opspot.controller;

import com.opspot.dto.common.ImportRequest;
import com.opspot.dto.common.ImportSummary;
import com.opspot.dto.common.StatusUpdateRequest;
import com.opspot.dto.marathon.MarathonFilterParams;
import com.opspot.dto.marathon.MarathonResponseDTO;
import com.opspot.service.MarathonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marathons")
@RequiredArgsConstructor
public class MarathonController {

    private final MarathonService marathonService;

    @PostMapping("/import")
    public ResponseEntity<ImportSummary> importMarathons(@Valid @RequestBody ImportRequest request) {
        return ResponseEntity.ok(marathonService.importFromJson(request.getJson()));
    }

    @GetMapping
    public ResponseEntity<List<MarathonResponseDTO>> getMarathons(@ModelAttribute MarathonFilterParams params) {
        return ResponseEntity.ok(marathonService.getMarathons(params));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<MarathonResponseDTO> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(marathonService.updateStatus(id, request.getStatus()));
    }
}
