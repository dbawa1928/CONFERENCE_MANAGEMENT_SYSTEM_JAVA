package com.confsync.repository;

import com.confsync.entity.Speaker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpeakerRepository extends JpaRepository<Speaker, Long> {
    Optional<Speaker> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}

