package com.confsync.repository;

import com.confsync.entity.Conference;
import com.confsync.enums.ConferenceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, Long> {

    List<Conference> findByStatus(ConferenceStatus status);

    List<Conference> findByStartDateGreaterThanEqualOrderByStartDateAsc(LocalDate date);

    long countByStatus(ConferenceStatus status);
}


