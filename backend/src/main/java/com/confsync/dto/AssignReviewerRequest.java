package com.confsync.dto;

import jakarta.validation.constraints.NotNull;

public class AssignReviewerRequest {
    @NotNull(message = "Paper ID is required")
    private Long paperId;

    @NotNull(message = "Reviewer ID is required")
    private Long reviewerId;

    public AssignReviewerRequest() {}

    public AssignReviewerRequest(Long paperId, Long reviewerId) {
        this.paperId = paperId;
        this.reviewerId = reviewerId;
    }

    public Long getPaperId() { return paperId; }
    public void setPaperId(Long paperId) { this.paperId = paperId; }
    public Long getReviewerId() { return reviewerId; }
    public void setReviewerId(Long reviewerId) { this.reviewerId = reviewerId; }
}

