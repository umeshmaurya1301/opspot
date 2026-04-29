package com.opspot.controller;

import com.opspot.dto.common.ImportRequest;
import com.opspot.dto.common.ImportSummary;
import com.opspot.dto.common.StatusUpdateRequest;
import com.opspot.dto.job.JobFilterParams;
import com.opspot.dto.job.JobResponseDTO;
import com.opspot.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping("/import")
    public ResponseEntity<ImportSummary> importJobs(@Valid @RequestBody ImportRequest request) {
        return ResponseEntity.ok(jobService.importFromJson(request.getJson()));
    }

    @GetMapping
    public ResponseEntity<List<JobResponseDTO>> getJobs(@ModelAttribute JobFilterParams params) {
        return ResponseEntity.ok(jobService.getJobs(params));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<JobResponseDTO> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(jobService.updateStatus(id, request.getStatus()));
    }
}
