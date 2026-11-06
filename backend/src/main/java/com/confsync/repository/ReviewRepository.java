package com.confsync.repository;

import com.confsync.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByPaperId(Long paperId);
    List<Review> findByReviewerId(Long reviewerId);
    Optional<Review> findByPaperIdAndReviewerId(Long paperId, Long reviewerId);
    boolean existsByPaperIdAndReviewerId(Long paperId, Long reviewerId);
}

