package com.confsync.dto;

import com.confsync.entity.Registration;
import com.confsync.enums.RegistrationStatus;
import java.time.LocalDateTime;

public class RegistrationResponse {
    private Long id;
    private UserResponse participant;
    private ConferenceResponse conference;
    private LocalDateTime registrationDate;
    private RegistrationStatus status;
    private Boolean attendanceStatus;
    private String qrCode;

    public RegistrationResponse() {}

    public static RegistrationResponse fromEntity(Registration reg, ConferenceResponse confResp) {
        if (reg == null) return null;
        RegistrationResponse resp = new RegistrationResponse();
        resp.setId(reg.getId());
        resp.setParticipant(UserResponse.fromEntity(reg.getParticipant()));
        resp.setConference(confResp);
        resp.setRegistrationDate(reg.getRegistrationDate());
        resp.setStatus(reg.getStatus());
        resp.setAttendanceStatus(reg.getAttendanceStatus());
        resp.setQrCode(reg.getQrCode());
        return resp;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public UserResponse getParticipant() { return participant; }
    public void setParticipant(UserResponse participant) { this.participant = participant; }
    public ConferenceResponse getConference() { return conference; }
    public void setConference(ConferenceResponse conference) { this.conference = conference; }
    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }
    public RegistrationStatus getStatus() { return status; }
    public void setStatus(RegistrationStatus status) { this.status = status; }
    public Boolean getAttendanceStatus() { return attendanceStatus; }
    public void setAttendanceStatus(Boolean attendanceStatus) { this.attendanceStatus = attendanceStatus; }
    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }
}

