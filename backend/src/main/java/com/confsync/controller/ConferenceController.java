package com.confsync.controller;

import com.confsync.dto.ApiResponse;
import com.confsync.dto.ConferenceRequest;
import com.confsync.dto.ConferenceResponse;
import com.confsync.enums.ConferenceStatus;
import com.confsync.service.ConferenceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conferences")
public class ConferenceController {

    @Autowired
    private ConferenceService conferenceService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ConferenceResponse>>> getAllConferences(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) ConferenceStatus status) {
        List<ConferenceResponse> conferences = conferenceService.searchConferences(search, category, status);
        return ResponseEntity.ok(ApiResponse.success("Conferences retrieved", conferences));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<ApiResponse<List<ConferenceResponse>>> getUpcomingConferences() {
        List<ConferenceResponse> conferences = conferenceService.getUpcomingConferences();
        return ResponseEntity.ok(ApiResponse.success("Upcoming conferences retrieved", conferences));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ConferenceResponse>> getConferenceById(@PathVariable Long id) {
        ConferenceResponse conference = conferenceService.getConferenceById(id);
        return ResponseEntity.ok(ApiResponse.success("Conference retrieved", conference));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ConferenceResponse>> createConference(
            @Valid @RequestBody ConferenceRequest request) {
        ConferenceResponse created = conferenceService.createConference(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Conference created successfully", created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ConferenceResponse>> updateConference(
            @PathVariable Long id,
            @Valid @RequestBody ConferenceRequest request) {
        ConferenceResponse updated = conferenceService.updateConference(id, request);
        return ResponseEntity.ok(ApiResponse.success("Conference updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteConference(@PathVariable Long id) {
        conferenceService.deleteConference(id);
        return ResponseEntity.ok(ApiResponse.success("Conference deleted successfully"));
    }
}

