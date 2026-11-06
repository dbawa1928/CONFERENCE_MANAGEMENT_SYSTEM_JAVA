package com.confsync.service;

import com.confsync.dto.AssignReviewerRequest;
import com.confsync.dto.ReviewRequest;
import com.confsync.dto.ReviewResponse;
import com.confsync.dto.ReviewerAssignmentResponse;

import java.util.List;

public interface ReviewService {
    ReviewerAssignmentResponse assignReviewer(AssignReviewerRequest request);
    List<ReviewerAssignmentResponse> getAssignedPapersForCurrentReviewer();
    List<ReviewerAssignmentResponse> getAssignmentsForPaper(Long paperId);
    ReviewResponse submitReview(Long paperId, ReviewRequest request);
    List<ReviewResponse> getReviewsForPaper(Long paperId);
    List<ReviewResponse> getReviewsByCurrentReviewer();
}

