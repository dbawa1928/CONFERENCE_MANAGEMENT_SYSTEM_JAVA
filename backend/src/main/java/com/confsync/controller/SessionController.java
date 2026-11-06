package com.confsync.controller;

import com.confsync.dto.ApiResponse;
import com.confsync.dto.SessionRequest;
import com.confsync.dto.SessionResponse;
import com.confsync.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SessionResponse>>> getAllSessions() {
        List<SessionResponse> sessions = sessionService.getAllSessions();
        return ResponseEntity.ok(ApiResponse.success("Sessions retrieved", sessions));
    }

    @GetMapping("/conference/{conferenceId}")
    public ResponseEntity<ApiResponse<List<SessionResponse>>> getSessionsByConference(@PathVariable Long conferenceId) {
        List<SessionResponse> sessions = sessionService.getSessionsByConferenceId(conferenceId);
        return ResponseEntity.ok(ApiResponse.success("Conference sessions retrieved", sessions));
    }

    @GetMapping("/speaker/{speakerId}")
    public ResponseEntity<ApiResponse<List<SessionResponse>>> getSessionsBySpeaker(@PathVariable Long speakerId) {
        List<SessionResponse> sessions = sessionService.getSessionsBySpeakerId(speakerId);
        return ResponseEntity.ok(ApiResponse.success("Speaker sessions retrieved", sessions));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SessionResponse>> getSessionById(@PathVariable Long id) {
        SessionResponse session = sessionService.getSessionById(id);
        return ResponseEntity.ok(ApiResponse.success("Session retrieved", session));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SessionResponse>> createSession(@Valid @RequestBody SessionRequest request) {
        SessionResponse created = sessionService.createSession(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Session created successfully", created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SessionResponse>> updateSession(
            @PathVariable Long id,
            @Valid @RequestBody SessionRequest request) {
        SessionResponse updated = sessionService.updateSession(id, request);
        return ResponseEntity.ok(ApiResponse.success("Session updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteSession(@PathVariable Long id) {
        sessionService.deleteSession(id);
        return ResponseEntity.ok(ApiResponse.success("Session deleted successfully"));
    }
}

