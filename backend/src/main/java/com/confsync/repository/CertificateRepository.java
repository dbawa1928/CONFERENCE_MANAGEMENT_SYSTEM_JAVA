package com.confsync.repository;

import com.confsync.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    List<Certificate> findByParticipantId(Long participantId);
    Optional<Certificate> findByParticipantIdAndConferenceId(Long participantId, Long conferenceId);
    boolean existsByParticipantIdAndConferenceId(Long participantId, Long conferenceId);
    Optional<Certificate> findByCertificateNumber(String certificateNumber);
    Optional<Certificate> findByVerificationCode(String verificationCode);
}

