package com.confsync.dto;

import com.confsync.entity.Review;
import com.confsync.enums.Recommendation;
import java.time.LocalDateTime;

public class ReviewResponse {
    private Long id;
    private Long paperId;
    private String paperTitle;
    private UserResponse reviewer;
    private Double originalityScore;
    private Double technicalQualityScore;
    private Double relevanceScore;
    private Double presentationScore;
    private Double overallScore;
    private String comments;
    private Recommendation recommendation;
    private LocalDateTime reviewedAt;

    public ReviewResponse() {}

    public static ReviewResponse fromEntity(Review r) {
        if (r == null) return null;
        ReviewResponse resp = new ReviewResponse();
        resp.setId(r.getId());
        resp.setPaperId(r.getPaper().getId());
        resp.setPaperTitle(r.getPaper().getTitle());
        resp.setReviewer(UserResponse.fromEntity(r.getReviewer()));
        resp.setOriginalityScore(r.getOriginalityScore());
        resp.setTechnicalQualityScore(r.getTechnicalQualityScore());
        resp.setRelevanceScore(r.getRelevanceScore());
        resp.setPresentationScore(r.getPresentationScore());
        resp.setOverallScore(r.getOverallScore());
        resp.setComments(r.getComments());
        resp.setRecommendation(r.getRecommendation());
        resp.setReviewedAt(r.getReviewedAt());
        return resp;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPaperId() { return paperId; }
    public void setPaperId(Long paperId) { this.paperId = paperId; }
    public String getPaperTitle() { return paperTitle; }
    public void setPaperTitle(String paperTitle) { this.paperTitle = paperTitle; }
    public UserResponse getReviewer() { return reviewer; }
    public void setReviewer(UserResponse reviewer) { this.reviewer = reviewer; }
    public Double getOriginalityScore() { return originalityScore; }
    public void setOriginalityScore(Double originalityScore) { this.originalityScore = originalityScore; }
    public Double getTechnicalQualityScore() { return technicalQualityScore; }
    public void setTechnicalQualityScore(Double technicalQualityScore) { this.technicalQualityScore = technicalQualityScore; }
    public Double getRelevanceScore() { return relevanceScore; }
    public void setRelevanceScore(Double relevanceScore) { this.relevanceScore = relevanceScore; }
    public Double getPresentationScore() { return presentationScore; }
    public void setPresentationScore(Double presentationScore) { this.presentationScore = presentationScore; }
    public Double getOverallScore() { return overallScore; }
    public void setOverallScore(Double overallScore) { this.overallScore = overallScore; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    public Recommendation getRecommendation() { return recommendation; }
    public void setRecommendation(Recommendation recommendation) { this.recommendation = recommendation; }
    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }
}

