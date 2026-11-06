package com.confsync.service;

import com.confsync.dto.RegistrationResponse;
import com.confsync.entity.Registration;

import java.util.List;

public interface RegistrationService {
    RegistrationResponse registerForConference(Long conferenceId);
    List<RegistrationResponse> getMyRegistrations();
    List<RegistrationResponse> getConferenceRegistrations(Long conferenceId);
    RegistrationResponse cancelRegistration(Long registrationId);
    RegistrationResponse getRegistrationById(Long id);
    Registration getRegistrationByQrCode(String qrCode);
}

