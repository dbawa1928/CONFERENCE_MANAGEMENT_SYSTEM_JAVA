package com.confsync.dto;

import com.confsync.entity.Certificate;
import java.time.LocalDateTime;

public class CertificateResponse {
    private Long id;
    private Long participantId;
    private String participantName;
    private Long conferenceId;
    private String conferenceTitle;
    private String certificateNumber;
    private String verificationCode;
    private LocalDateTime generatedAt;
    private String downloadUrl;

    public CertificateResponse() {}

    public static CertificateResponse fromEntity(Certificate c) {
        if (c == null) return null;
        CertificateResponse resp = new CertificateResponse();
        resp.setId(c.getId());
        resp.setParticipantId(c.getParticipant().getId());
        resp.setParticipantName(c.getParticipant().getName());
        resp.setConferenceId(c.getConference().getId());
        resp.setConferenceTitle(c.getConference().getTitle());
        resp.setCertificateNumber(c.getCertificateNumber());
        resp.setVerificationCode(c.getVerificationCode());
        resp.setGeneratedAt(c.getGeneratedAt());
        resp.setDownloadUrl("/api/certificates/" + c.getId() + "/download");
        return resp;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getParticipantId() { return participantId; }
    public void setParticipantId(Long participantId) { this.participantId = participantId; }
    public String getParticipantName() { return participantName; }
    public void setParticipantName(String participantName) { this.participantName = participantName; }
    public Long getConferenceId() { return conferenceId; }
    public void setConferenceId(Long conferenceId) { this.conferenceId = conferenceId; }
    public String getConferenceTitle() { return conferenceTitle; }
    public void setConferenceTitle(String conferenceTitle) { this.conferenceTitle = conferenceTitle; }
    public String getCertificateNumber() { return certificateNumber; }
    public void setCertificateNumber(String certificateNumber) { this.certificateNumber = certificateNumber; }
    public String getVerificationCode() { return verificationCode; }
    public void setVerificationCode(String verificationCode) { this.verificationCode = verificationCode; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
    public String getDownloadUrl() { return downloadUrl; }
    public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }
}

