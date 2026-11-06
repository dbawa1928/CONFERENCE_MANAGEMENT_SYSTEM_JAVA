package com.confsync.dto;

import com.confsync.enums.Recommendation;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ReviewRequest {
    @NotNull(message = "Originality score is required")
    @Min(value = 1, message = "Originality score must be between 1 and 10")
    @Max(value = 10, message = "Originality score must be between 1 and 10")
    private Double originalityScore;

    @NotNull(message = "Technical Quality score is required")
    @Min(value = 1, message = "Technical Quality score must be between 1 and 10")
    @Max(value = 10, message = "Technical Quality score must be between 1 and 10")
    private Double technicalQualityScore;

    @NotNull(message = "Relevance score is required")
    @Min(value = 1, message = "Relevance score must be between 1 and 10")
    @Max(value = 10, message = "Relevance score must be between 1 and 10")
    private Double relevanceScore;

    @NotNull(message = "Presentation score is required")
    @Min(value = 1, message = "Presentation score must be between 1 and 10")
    @Max(value = 10, message = "Presentation score must be between 1 and 10")
    private Double presentationScore;

    private String comments;

    @NotNull(message = "Recommendation is required")
    private Recommendation recommendation;

    public ReviewRequest() {}

    public Double getOriginalityScore() { return originalityScore; }
    public void setOriginalityScore(Double originalityScore) { this.originalityScore = originalityScore; }
    public Double getTechnicalQualityScore() { return technicalQualityScore; }
    public void setTechnicalQualityScore(Double technicalQualityScore) { this.technicalQualityScore = technicalQualityScore; }
    public Double getRelevanceScore() { return relevanceScore; }
    public void setRelevanceScore(Double relevanceScore) { this.relevanceScore = relevanceScore; }
    public Double getPresentationScore() { return presentationScore; }
    public void setPresentationScore(Double presentationScore) { this.presentationScore = presentationScore; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    public Recommendation getRecommendation() { return recommendation; }
    public void setRecommendation(Recommendation recommendation) { this.recommendation = recommendation; }
}

