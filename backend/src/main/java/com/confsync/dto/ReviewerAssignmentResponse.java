package com.confsync.dto;

import com.confsync.entity.ReviewerAssignment;
import com.confsync.enums.AssignmentStatus;
import java.time.LocalDateTime;

public class ReviewerAssignmentResponse {
    private Long id;
    private PaperResponse paper;
    private UserResponse reviewer;
    private LocalDateTime assignedAt;
    private AssignmentStatus status;

    public ReviewerAssignmentResponse() {}

    public static ReviewerAssignmentResponse fromEntity(ReviewerAssignment ra) {
        if (ra == null) return null;
        ReviewerAssignmentResponse resp = new ReviewerAssignmentResponse();
        resp.setId(ra.getId());
        resp.setPaper(PaperResponse.fromEntity(ra.getPaper()));
        resp.setReviewer(UserResponse.fromEntity(ra.getReviewer()));
        resp.setAssignedAt(ra.getAssignedAt());
        resp.setStatus(ra.getStatus());
        return resp;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public PaperResponse getPaper() { return paper; }
    public void setPaper(PaperResponse paper) { this.paper = paper; }
    public UserResponse getReviewer() { return reviewer; }
    public void setReviewer(UserResponse reviewer) { this.reviewer = reviewer; }
    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }
    public AssignmentStatus getStatus() { return status; }
    public void setStatus(AssignmentStatus status) { this.status = status; }
}

