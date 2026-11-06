package com.confsync.controller;

import com.confsync.dto.ApiResponse;
import com.confsync.dto.AttendanceCheckInRequest;
import com.confsync.dto.RegistrationResponse;
import com.confsync.service.AttendanceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/check-in")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RegistrationResponse>> markAttendance(
            @Valid @RequestBody AttendanceCheckInRequest request) {
        RegistrationResponse response = attendanceService.markAttendance(request);
        return ResponseEntity.ok(ApiResponse.success("Attendance verified and checked in successfully", response));
    }
}

