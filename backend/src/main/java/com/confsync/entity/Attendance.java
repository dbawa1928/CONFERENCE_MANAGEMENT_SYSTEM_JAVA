package com.confsync.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Attendance Entity recording verified check-in via QR code or admin check-in.
 */
@Entity
@Table(name = "attendance")
public class Attendance extends BaseEntity {

    @NotNull(message = "Registration is required")
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "registration_id", nullable = false, unique = true)
    private Registration registration;

    @Column(name = "check_in_time", nullable = false)
    private LocalDateTime checkInTime;

    public Attendance() {
        super();
        this.checkInTime = LocalDateTime.now();
    }

    public Attendance(Registration registration) {
        this();
        this.registration = registration;
    }

    // Getters and Setters
    public Registration getRegistration() {
        return registration;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }
}

