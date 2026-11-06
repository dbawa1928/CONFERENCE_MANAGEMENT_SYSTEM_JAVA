package com.confsync.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Certificate Entity representing conference participation certificate.
 */
@Entity
@Table(name = "certificates", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"participant_id", "conference_id"})
})
public class Certificate extends BaseEntity {

    @NotNull(message = "Participant is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "participant_id", nullable = false)
    private User participant;

    @NotNull(message = "Conference is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "conference_id", nullable = false)
    private Conference conference;

    @NotBlank(message = "Certificate number is required")
    @Column(name = "certificate_number", nullable = false, unique = true)
    private String certificateNumber;

    @NotBlank(message = "Verification code is required")
    @Column(name = "verification_code", nullable = false, unique = true)
    private String verificationCode;

    @Column(name = "generated_at", nullable = false)
    private LocalDateTime generatedAt;

    @Column(name = "file_path")
    private String filePath;

    public Certificate() {
        super();
        this.generatedAt = LocalDateTime.now();
    }

    public Certificate(User participant, Conference conference, String certificateNumber,
                       String verificationCode, String filePath) {
        this();
        this.participant = participant;
        this.conference = conference;
        this.certificateNumber = certificateNumber;
        this.verificationCode = verificationCode;
        this.filePath = filePath;
    }

    // Getters and Setters
    public User getParticipant() {
        return participant;
    }

    public void setParticipant(User participant) {
        this.participant = participant;
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}

