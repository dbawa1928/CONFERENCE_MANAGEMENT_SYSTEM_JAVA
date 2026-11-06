package com.confsync.service;

import com.confsync.dto.AttendanceCheckInRequest;
import com.confsync.dto.RegistrationResponse;

public interface AttendanceService {
    RegistrationResponse markAttendance(AttendanceCheckInRequest request);
    boolean hasAttended(Long registrationId);
}

