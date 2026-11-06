package com.confsync.service.impl;

import com.confsync.dto.AssignReviewerRequest;
import com.confsync.dto.ReviewRequest;
import com.confsync.dto.ReviewResponse;
import com.confsync.dto.ReviewerAssignmentResponse;
import com.confsync.entity.Paper;
import com.confsync.entity.Review;
import com.confsync.entity.ReviewerAssignment;
import com.confsync.entity.User;
import com.confsync.enums.AssignmentStatus;
import com.confsync.enums.PaperStatus;
import com.confsync.enums.Role;
import com.confsync.exception.BadRequestException;
import com.confsync.exception.DuplicateRegistrationException;
import com.confsync.exception.ResourceNotFoundException;
import com.confsync.repository.PaperRepository;
import com.confsync.repository.ReviewRepository;
import com.confsync.repository.ReviewerAssignmentRepository;
import com.confsync.service.NotificationService;
import com.confsync.service.PaperService;
import com.confsync.service.ReviewService;
import com.confsync.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewerAssignmentRepository assignmentRepository;

    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private PaperService paperService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Override
    public ReviewerAssignmentResponse assignReviewer(AssignReviewerRequest request) {
        Paper paper = paperService.getPaperEntityById(request.getPaperId());
        User reviewer = userService.getUserById(request.getReviewerId());

        if (reviewer.getRole() != Role.REVIEWER && reviewer.getRole() != Role.ADMIN) {
            throw new BadRequestException("Selected user is not registered as a REVIEWER");
        }

        if (paper.getAuthor().getId().equals(reviewer.getId())) {
            throw new BadRequestException("An author cannot be assigned to review their own paper");
        }

        if (assignmentRepository.existsByPaperIdAndReviewerId(paper.getId(), reviewer.getId())) {
            throw new DuplicateRegistrationException("Reviewer is already assigned to this paper");
        }

        ReviewerAssignment assignment = new ReviewerAssignment(paper, reviewer);
        ReviewerAssignment saved = assignmentRepository.save(assignment);

        // Update paper status to UNDER_REVIEW if currently SUBMITTED
        if (paper.getStatus() == PaperStatus.SUBMITTED) {
            paper.setStatus(PaperStatus.UNDER_REVIEW);
            paperRepository.save(paper);
        }

        // Notify reviewer
        notificationService.sendNotification(reviewer,
                "New Paper Assigned for Review",
                "You have been assigned to review paper: '" + paper.getTitle() + "'.");

        return ReviewerAssignmentResponse.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewerAssignmentResponse> getAssignedPapersForCurrentReviewer() {
        User currentUser = userService.getCurrentUserEntity();
        return assignmentRepository.findByReviewerId(currentUser.getId()).stream()
                .map(ReviewerAssignmentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewerAssignmentResponse> getAssignmentsForPaper(Long paperId) {
        return assignmentRepository.findByPaperId(paperId).stream()
                .map(ReviewerAssignmentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public ReviewResponse submitReview(Long paperId, ReviewRequest request) {
        User currentUser = userService.getCurrentUserEntity();
        Paper paper = paperService.getPaperEntityById(paperId);

        Optional<Review> existingReview = reviewRepository.findByPaperIdAndReviewerId(paper.getId(), currentUser.getId());
        Review review;
        if (existingReview.isPresent()) {
            review = existingReview.get();
            review.setOriginalityScore(request.getOriginalityScore());
            review.setTechnicalQualityScore(request.getTechnicalQualityScore());
            review.setRelevanceScore(request.getRelevanceScore());
            review.setPresentationScore(request.getPresentationScore());
            review.setComments(request.getComments());
            review.setRecommendation(request.getRecommendation());
            review.setReviewedAt(LocalDateTime.now());
        } else {
            review = new Review(
                    paper,
                    currentUser,
                    request.getOriginalityScore(),
                    request.getTechnicalQualityScore(),
                    request.getRelevanceScore(),
                    request.getPresentationScore(),
                    request.getComments(),
                    request.getRecommendation()
            );
        }

        Review saved = reviewRepository.save(review);

        // Mark assignment as COMPLETED
        Optional<ReviewerAssignment> assignmentOpt = assignmentRepository.findByPaperIdAndReviewerId(paper.getId(), currentUser.getId());
        if (assignmentOpt.isPresent()) {
            ReviewerAssignment assignment = assignmentOpt.get();
            assignment.setStatus(AssignmentStatus.COMPLETED);
            assignmentRepository.save(assignment);
        }

        // Notify paper author
        notificationService.sendNotification(paper.getAuthor(),
                "Peer Review Submitted",
                "A reviewer has submitted feedback on your paper: '" + paper.getTitle() + "'.");

        return ReviewResponse.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsForPaper(Long paperId) {
        return reviewRepository.findByPaperId(paperId).stream()
                .map(ReviewResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByCurrentReviewer() {
        User currentUser = userService.getCurrentUserEntity();
        return reviewRepository.findByReviewerId(currentUser.getId()).stream()
                .map(ReviewResponse::fromEntity)
                .collect(Collectors.toList());
    }
}

