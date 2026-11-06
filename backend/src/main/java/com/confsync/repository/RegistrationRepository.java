package com.confsync.repository;

import com.confsync.entity.Registration;
import com.confsync.enums.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByParticipantId(Long participantId);
    List<Registration> findByConferenceId(Long conferenceId);
    Optional<Registration> findByParticipantIdAndConferenceId(Long participantId, Long conferenceId);
    boolean existsByParticipantIdAndConferenceId(Long participantId, Long conferenceId);
    long countByConferenceIdAndStatus(Long conferenceId, RegistrationStatus status);
    Optional<Registration> findByQrCode(String qrCode);
    long countByAttendanceStatus(Boolean attendanceStatus);
}

