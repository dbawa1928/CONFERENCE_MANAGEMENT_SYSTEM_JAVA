package com.confsync.controller;

import com.confsync.dto.ApiResponse;
import com.confsync.dto.RegistrationResponse;
import com.confsync.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<ApiResponse<RegistrationResponse>> registerForConference(
            @RequestBody Map<String, Long> payload) {
        Long conferenceId = payload.get("conferenceId");
        RegistrationResponse response = registrationService.registerForConference(conferenceId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Successfully registered for conference", response));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<RegistrationResponse>>> getMyRegistrations() {
        List<RegistrationResponse> list = registrationService.getMyRegistrations();
        return ResponseEntity.ok(ApiResponse.success("User registrations retrieved", list));
    }

    @GetMapping("/conference/{conferenceId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<RegistrationResponse>>> getConferenceRegistrations(
            @PathVariable Long conferenceId) {
        List<RegistrationResponse> list = registrationService.getConferenceRegistrations(conferenceId);
        return ResponseEntity.ok(ApiResponse.success("Conference registrations retrieved", list));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<RegistrationResponse>> cancelRegistration(@PathVariable Long id) {
        RegistrationResponse response = registrationService.cancelRegistration(id);
        return ResponseEntity.ok(ApiResponse.success("Registration cancelled successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RegistrationResponse>> getRegistrationById(@PathVariable Long id) {
        RegistrationResponse response = registrationService.getRegistrationById(id);
        return ResponseEntity.ok(ApiResponse.success("Registration retrieved", response));
    }
}

