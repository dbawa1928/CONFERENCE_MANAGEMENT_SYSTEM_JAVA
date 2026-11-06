package com.confsync.repository;

import com.confsync.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByConferenceId(Long conferenceId);
    List<Session> findBySpeakerId(Long speakerId);
    List<Session> findBySessionDate(LocalDate sessionDate);
    List<Session> findByConferenceIdOrderBySessionDateAscStartTimeAsc(Long conferenceId);
}

