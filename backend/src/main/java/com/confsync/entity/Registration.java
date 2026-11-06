package com.confsync.entity;

import com.confsync.enums.RegistrationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Registration Entity representing a participant's ticket/registration to a conference.
 */
@Entity
@Table(name = "registrations", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"participant_id", "conference_id"})
})
public class Registration extends BaseEntity {

    @NotNull(message = "Participant is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "participant_id", nullable = false)
    private User participant;

    @NotNull(message = "Conference is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "conference_id", nullable = false)
    private Conference conference;

    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private RegistrationStatus status;

    @Column(name = "attendance_status", nullable = false)
    private Boolean attendanceStatus = false;

    @Column(name = "qr_code", columnDefinition = "TEXT")
    private String qrCode;

    public Registration() {
        super();
        this.status = RegistrationStatus.REGISTERED;
        this.attendanceStatus = false;
        this.registrationDate = LocalDateTime.now();
    }

    public Registration(User participant, Conference conference, String qrCode) {
        this();
        this.participant = participant;
        this.conference = conference;
        this.qrCode = qrCode;
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

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public RegistrationStatus getStatus() {
        return status;
    }

    public void setStatus(RegistrationStatus status) {
        this.status = status;
    }

    public Boolean getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(Boolean attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}

