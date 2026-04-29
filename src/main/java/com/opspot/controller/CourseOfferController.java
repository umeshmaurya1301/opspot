package com.opspot.controller;

import com.opspot.dto.common.ImportRequest;
import com.opspot.dto.common.ImportSummary;
import com.opspot.dto.common.StatusUpdateRequest;
import com.opspot.dto.course.CourseOfferFilterParams;
import com.opspot.dto.course.CourseOfferResponseDTO;
import com.opspot.service.CourseOfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course-offers")
@RequiredArgsConstructor
public class CourseOfferController {

    private final CourseOfferService courseOfferService;

    @PostMapping("/import")
    public ResponseEntity<ImportSummary> importCourseOffers(@Valid @RequestBody ImportRequest request) {
        return ResponseEntity.ok(courseOfferService.importFromJson(request.getJson()));
    }

    @GetMapping
    public ResponseEntity<List<CourseOfferResponseDTO>> getCourseOffers(@ModelAttribute CourseOfferFilterParams params) {
        return ResponseEntity.ok(courseOfferService.getCourseOffers(params));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CourseOfferResponseDTO> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(courseOfferService.updateStatus(id, request.getStatus()));
    }
}
