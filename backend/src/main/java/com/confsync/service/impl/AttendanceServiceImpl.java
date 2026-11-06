package com.confsync.service.impl;

import com.confsync.dto.AttendanceCheckInRequest;
import com.confsync.dto.ConferenceResponse;
import com.confsync.dto.RegistrationResponse;
import com.confsync.entity.Attendance;
import com.confsync.entity.Registration;
import com.confsync.enums.RegistrationStatus;
import com.confsync.exception.BadRequestException;
import com.confsync.exception.ResourceNotFoundException;
import com.confsync.repository.AttendanceRepository;
import com.confsync.repository.RegistrationRepository;
import com.confsync.service.AttendanceService;
import com.confsync.service.ConferenceService;
import com.confsync.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private ConferenceService conferenceService;

    @Autowired
    private NotificationService notificationService;

    @Override
    public RegistrationResponse markAttendance(AttendanceCheckInRequest request) {
        String token = request.getQrCode().trim();
        Registration registration = null;

        // Attempt 1: Check by QR code
        var regOpt = registrationRepository.findByQrCode(token);
        if (regOpt.isPresent()) {
            registration = regOpt.get();
        } else {
            // Attempt 2: Check by numeric Registration ID
            try {
                Long id = Long.parseLong(token);
                registration = registrationRepository.findById(id).orElse(null);
            } catch (NumberFormatException ignored) {}
        }

        if (registration == null) {
            throw new ResourceNotFoundException("No valid registration record found for token: " + token);
        }

        if (registration.getStatus() == RegistrationStatus.CANCELLED) {
            throw new BadRequestException("Registration has been cancelled and cannot be checked in");
        }

        // Prevent duplicate attendance marking
        if (attendanceRepository.existsByRegistrationId(registration.getId())) {
            throw new BadRequestException("Attendance already verified and marked for participant: "
                    + registration.getParticipant().getName());
        }

        Attendance attendance = new Attendance(registration);
        attendanceRepository.save(attendance);

        registration.setAttendanceStatus(true);
        Registration updated = registrationRepository.save(registration);

        notificationService.sendNotification(registration.getParticipant(),
                "Attendance Confirmed",
                "Your attendance has been marked for " + registration.getConference().getTitle() + ". You are now eligible for your Certificate!");

        ConferenceResponse confResp = conferenceService.getConferenceById(registration.getConference().getId());
        return RegistrationResponse.fromEntity(updated, confResp);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasAttended(Long registrationId) {
        return attendanceRepository.existsByRegistrationId(registrationId);
    }
}

