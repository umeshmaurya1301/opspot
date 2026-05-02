package com.opspot.controller;

import com.opspot.dto.common.ImportRequest;
import com.opspot.dto.common.ImportSummary;
import com.opspot.dto.common.StatusUpdateRequest;
import com.opspot.dto.event.EventFilterParams;
import com.opspot.dto.event.EventResponseDTO;
import com.opspot.dto.event.HackathonEventDetailsResponseDTO;
import com.opspot.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping("/import")
    public ResponseEntity<ImportSummary> importEvents(@Valid @RequestBody ImportRequest request) {
        return ResponseEntity.ok(eventService.importFromJson(request.getJson()));
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDTO>> getEvents(@ModelAttribute EventFilterParams params) {
        return ResponseEntity.ok(eventService.getEvents(params));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HackathonEventDetailsResponseDTO> getEventDetails(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventDetails(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<EventResponseDTO> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(eventService.updateStatus(id, request.getStatus()));
    }
}
