package com.confsync.entity;

import com.confsync.enums.AssignmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Entity tracking reviewer assignment to research papers.
 */
@Entity
@Table(name = "reviewer_assignments", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"paper_id", "reviewer_id"})
})
public class ReviewerAssignment extends BaseEntity {

    @NotNull(message = "Paper is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paper_id", nullable = false)
    private Paper paper;

    @NotNull(message = "Reviewer is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private AssignmentStatus status;

    public ReviewerAssignment() {
        super();
        this.assignedAt = LocalDateTime.now();
        this.status = AssignmentStatus.PENDING;
    }

    public ReviewerAssignment(Paper paper, User reviewer) {
        this();
        this.paper = paper;
        this.reviewer = reviewer;
    }

    // Getters and Setters
    public Paper getPaper() {
        return paper;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public AssignmentStatus getStatus() {
        return status;
    }

    public void setStatus(AssignmentStatus status) {
        this.status = status;
    }
}

