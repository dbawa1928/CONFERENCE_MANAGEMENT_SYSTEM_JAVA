package com.confsync.controller;

import com.confsync.dto.ApiResponse;
import com.confsync.dto.AssignReviewerRequest;
import com.confsync.dto.ReviewRequest;
import com.confsync.dto.ReviewResponse;
import com.confsync.dto.ReviewerAssignmentResponse;
import com.confsync.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ReviewerAssignmentResponse>> assignReviewer(
            @Valid @RequestBody AssignReviewerRequest request) {
        ReviewerAssignmentResponse response = reviewService.assignReviewer(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Reviewer assigned successfully", response));
    }

    @GetMapping("/assigned")
    @PreAuthorize("hasRole('REVIEWER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ReviewerAssignmentResponse>>> getAssignedPapers() {
        List<ReviewerAssignmentResponse> list = reviewService.getAssignedPapersForCurrentReviewer();
        return ResponseEntity.ok(ApiResponse.success("Assigned papers retrieved", list));
    }

    @GetMapping("/assignments/paper/{paperId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ReviewerAssignmentResponse>>> getAssignmentsForPaper(
            @PathVariable Long paperId) {
        List<ReviewerAssignmentResponse> list = reviewService.getAssignmentsForPaper(paperId);
        return ResponseEntity.ok(ApiResponse.success("Paper review assignments retrieved", list));
    }

    @PostMapping("/{paperId}")
    @PreAuthorize("hasRole('REVIEWER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ReviewResponse>> submitReview(
            @PathVariable Long paperId,
            @Valid @RequestBody ReviewRequest request) {
        ReviewResponse response = reviewService.submitReview(paperId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Review submitted successfully", response));
    }

    @GetMapping("/paper/{paperId}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviewsForPaper(@PathVariable Long paperId) {
        List<ReviewResponse> list = reviewService.getReviewsForPaper(paperId);
        return ResponseEntity.ok(ApiResponse.success("Paper reviews retrieved", list));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('REVIEWER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getMyReviews() {
        List<ReviewResponse> list = reviewService.getReviewsByCurrentReviewer();
        return ResponseEntity.ok(ApiResponse.success("Your reviews retrieved", list));
    }
}

