package com.confsync.entity;

import com.confsync.enums.Recommendation;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Review Entity representing a peer review evaluation of a research paper.
 */
@Entity
@Table(name = "reviews", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"paper_id", "reviewer_id"})
})
public class Review extends BaseEntity {

    @NotNull(message = "Paper is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paper_id", nullable = false)
    private Paper paper;

    @NotNull(message = "Reviewer is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    @Min(value = 1, message = "Originality score must be at least 1")
    @Max(value = 10, message = "Originality score must be at most 10")
    @Column(name = "originality_score", nullable = false)
    private Double originalityScore;

    @Min(value = 1, message = "Technical Quality score must be at least 1")
    @Max(value = 10, message = "Technical Quality score must be at most 10")
    @Column(name = "technical_quality_score", nullable = false)
    private Double technicalQualityScore;

    @Min(value = 1, message = "Relevance score must be at least 1")
    @Max(value = 10, message = "Relevance score must be at most 10")
    @Column(name = "relevance_score", nullable = false)
    private Double relevanceScore;

    @Min(value = 1, message = "Presentation score must be at least 1")
    @Max(value = 10, message = "Presentation score must be at most 10")
    @Column(name = "presentation_score", nullable = false)
    private Double presentationScore;

    @Column(name = "overall_score", nullable = false)
    private Double overallScore;

    @Lob
    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    @NotNull(message = "Recommendation is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "recommendation", nullable = false, length = 30)
    private Recommendation recommendation;

    @Column(name = "reviewed_at", nullable = false)
    private LocalDateTime reviewedAt;

    public Review() {
        super();
        this.reviewedAt = LocalDateTime.now();
    }

    public Review(Paper paper, User reviewer, Double originalityScore,
                  Double technicalQualityScore, Double relevanceScore,
                  Double presentationScore, String comments,
                  Recommendation recommendation) {
        this();
        this.paper = paper;
        this.reviewer = reviewer;
        this.originalityScore = originalityScore;
        this.technicalQualityScore = technicalQualityScore;
        this.relevanceScore = relevanceScore;
        this.presentationScore = presentationScore;
        this.comments = comments;
        this.recommendation = recommendation;
        calculateOverallScore();
    }

    /**
     * Calculates overall score = (Originality + Technical Quality + Relevance + Presentation) / 4.0
     */
    public void calculateOverallScore() {
        if (originalityScore != null && technicalQualityScore != null &&
                relevanceScore != null && presentationScore != null) {
            double avg = (originalityScore + technicalQualityScore + relevanceScore + presentationScore) / 4.0;
            this.overallScore = Math.round(avg * 100.0) / 100.0;
        }
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

    public Double getOriginalityScore() {
        return originalityScore;
    }

    public void setOriginalityScore(Double originalityScore) {
        this.originalityScore = originalityScore;
        calculateOverallScore();
    }

    public Double getTechnicalQualityScore() {
        return technicalQualityScore;
    }

    public void setTechnicalQualityScore(Double technicalQualityScore) {
        this.technicalQualityScore = technicalQualityScore;
        calculateOverallScore();
    }

    public Double getRelevanceScore() {
        return relevanceScore;
    }

    public void setRelevanceScore(Double relevanceScore) {
        this.relevanceScore = relevanceScore;
        calculateOverallScore();
    }

    public Double getPresentationScore() {
        return presentationScore;
    }

    public void setPresentationScore(Double presentationScore) {
        this.presentationScore = presentationScore;
        calculateOverallScore();
    }

    public Double getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(Double overallScore) {
        this.overallScore = overallScore;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Recommendation getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(Recommendation recommendation) {
        this.recommendation = recommendation;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }
}

