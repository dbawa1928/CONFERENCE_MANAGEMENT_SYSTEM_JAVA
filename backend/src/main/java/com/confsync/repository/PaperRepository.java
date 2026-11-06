package com.confsync.repository;

import com.confsync.entity.Paper;
import com.confsync.enums.PaperStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaperRepository extends JpaRepository<Paper, Long> {
    List<Paper> findByAuthorId(Long authorId);
    List<Paper> findByConferenceId(Long conferenceId);
    List<Paper> findByStatus(PaperStatus status);
    long countByStatus(PaperStatus status);
}

