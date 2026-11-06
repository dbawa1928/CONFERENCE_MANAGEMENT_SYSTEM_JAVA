package com.confsync.controller;

import com.confsync.dto.ApiResponse;
import com.confsync.dto.SpeakerRequest;
import com.confsync.dto.SpeakerResponse;
import com.confsync.service.SpeakerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/speakers")
public class SpeakerController {

    @Autowired
    private SpeakerService speakerService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SpeakerResponse>>> getAllSpeakers() {
        List<SpeakerResponse> speakers = speakerService.getAllSpeakers();
        return ResponseEntity.ok(ApiResponse.success("Speakers retrieved", speakers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SpeakerResponse>> getSpeakerById(@PathVariable Long id) {
        SpeakerResponse speaker = speakerService.getSpeakerById(id);
        return ResponseEntity.ok(ApiResponse.success("Speaker retrieved", speaker));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<SpeakerResponse>> getSpeakerByUserId(@PathVariable Long userId) {
        SpeakerResponse speaker = speakerService.getSpeakerByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success("Speaker profile retrieved", speaker));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SPEAKER')")
    public ResponseEntity<ApiResponse<SpeakerResponse>> createSpeaker(
            @Valid @RequestBody SpeakerRequest request) {
        SpeakerResponse created = speakerService.createSpeaker(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Speaker profile created", created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SPEAKER')")
    public ResponseEntity<ApiResponse<SpeakerResponse>> updateSpeaker(
            @PathVariable Long id,
            @Valid @RequestBody SpeakerRequest request) {
        SpeakerResponse updated = speakerService.updateSpeaker(id, request);
        return ResponseEntity.ok(ApiResponse.success("Speaker profile updated", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteSpeaker(@PathVariable Long id) {
        speakerService.deleteSpeaker(id);
        return ResponseEntity.ok(ApiResponse.success("Speaker deleted successfully"));
    }
}

