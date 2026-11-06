package com.confsync.repository;

import com.confsync.entity.ReviewerAssignment;
import com.confsync.enums.AssignmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewerAssignmentRepository extends JpaRepository<ReviewerAssignment, Long> {
    List<ReviewerAssignment> findByReviewerId(Long reviewerId);
    List<ReviewerAssignment> findByPaperId(Long paperId);
    Optional<ReviewerAssignment> findByPaperIdAndReviewerId(Long paperId, Long reviewerId);
    boolean existsByPaperIdAndReviewerId(Long paperId, Long reviewerId);
    long countByReviewerIdAndStatus(Long reviewerId, AssignmentStatus status);
}

