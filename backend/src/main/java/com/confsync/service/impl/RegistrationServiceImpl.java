package com.confsync.service.impl;

import com.confsync.dto.ConferenceResponse;
import com.confsync.dto.RegistrationResponse;
import com.confsync.entity.Conference;
import com.confsync.entity.Registration;
import com.confsync.entity.User;
import com.confsync.enums.RegistrationStatus;
import com.confsync.exception.ConferenceCapacityException;
import com.confsync.exception.DuplicateRegistrationException;
import com.confsync.exception.ResourceNotFoundException;
import com.confsync.repository.RegistrationRepository;
import com.confsync.service.ConferenceService;
import com.confsync.service.NotificationService;
import com.confsync.service.RegistrationService;
import com.confsync.service.UserService;
import com.confsync.util.QrCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class RegistrationServiceImpl implements RegistrationService {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private ConferenceService conferenceService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private QrCodeGenerator qrCodeGenerator;

    @Override
    public RegistrationResponse registerForConference(Long conferenceId) {
        User currentUser = userService.getCurrentUserEntity();
        Conference conference = conferenceService.getConferenceEntityById(conferenceId);

        // Check if already registered
        if (registrationRepository.existsByParticipantIdAndConferenceId(currentUser.getId(), conference.getId())) {
            throw new DuplicateRegistrationException("You are already registered for conference: " + conference.getTitle());
        }

        // Check maximum capacity
        long currentCount = registrationRepository.countByConferenceIdAndStatus(conference.getId(), RegistrationStatus.REGISTERED);
        if (currentCount >= conference.getMaxParticipants()) {
            throw new ConferenceCapacityException("Conference has reached maximum capacity of " + conference.getMaxParticipants() + " attendees");
        }

        // Generate unique QR verification payload
        String uniqueCode = "CS-REG-" + conference.getId() + "-" + currentUser.getId() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String qrBase64 = qrCodeGenerator.generateQrCodeBase64(uniqueCode, 300, 300);

        Registration reg = new Registration(currentUser, conference, qrBase64);
        Registration saved = registrationRepository.save(reg);

        // Notify user
        notificationService.sendNotification(currentUser,
                "Registration Confirmed: " + conference.getTitle(),
                "You have successfully registered! Your digital pass and QR code have been issued.");

        ConferenceResponse confResp = conferenceService.getConferenceById(conference.getId());
        return RegistrationResponse.fromEntity(saved, confResp);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegistrationResponse> getMyRegistrations() {
        User currentUser = userService.getCurrentUserEntity();
        return registrationRepository.findByParticipantId(currentUser.getId()).stream()
                .map(reg -> RegistrationResponse.fromEntity(reg, conferenceService.getConferenceById(reg.getConference().getId())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegistrationResponse> getConferenceRegistrations(Long conferenceId) {
        ConferenceResponse confResp = conferenceService.getConferenceById(conferenceId);
        return registrationRepository.findByConferenceId(conferenceId).stream()
                .map(reg -> RegistrationResponse.fromEntity(reg, confResp))
                .collect(Collectors.toList());
    }

    @Override
    public RegistrationResponse cancelRegistration(Long registrationId) {
        Registration reg = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Registration", "id", registrationId));

        reg.setStatus(RegistrationStatus.CANCELLED);
        Registration updated = registrationRepository.save(reg);

        notificationService.sendNotification(reg.getParticipant(),
                "Registration Cancelled",
                "Your registration for " + reg.getConference().getTitle() + " has been cancelled.");

        ConferenceResponse confResp = conferenceService.getConferenceById(reg.getConference().getId());
        return RegistrationResponse.fromEntity(updated, confResp);
    }

    @Override
    @Transactional(readOnly = true)
    public RegistrationResponse getRegistrationById(Long id) {
        Registration reg = registrationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registration", "id", id));
        ConferenceResponse confResp = conferenceService.getConferenceById(reg.getConference().getId());
        return RegistrationResponse.fromEntity(reg, confResp);
    }

    @Override
    @Transactional(readOnly = true)
    public Registration getRegistrationByQrCode(String qrCode) {
        return registrationRepository.findByQrCode(qrCode)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found with QR code: " + qrCode));
    }
}

