package com.confsync.dto;

import com.confsync.entity.Paper;
import com.confsync.enums.PaperStatus;
import java.time.LocalDateTime;

public class PaperResponse {
    private Long id;
    private UserResponse author;
    private Long conferenceId;
    private String conferenceTitle;
    private String title;
    private String abstractText;
    private String category;
    private String keywords;
    private String filePath;
    private PaperStatus status;
    private LocalDateTime submittedAt;
    private Double averageScore;
    private Integer reviewCount;

    public PaperResponse() {}

    public static PaperResponse fromEntity(Paper p) {
        if (p == null) return null;
        PaperResponse resp = new PaperResponse();
        resp.setId(p.getId());
        resp.setAuthor(UserResponse.fromEntity(p.getAuthor()));
        resp.setConferenceId(p.getConference().getId());
        resp.setConferenceTitle(p.getConference().getTitle());
        resp.setTitle(p.getTitle());
        resp.setAbstractText(p.getAbstractText());
        resp.setCategory(p.getCategory());
        resp.setKeywords(p.getKeywords());
        resp.setFilePath(p.getFilePath());
        resp.setStatus(p.getStatus());
        resp.setSubmittedAt(p.getSubmittedAt());
        return resp;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public UserResponse getAuthor() { return author; }
    public void setAuthor(UserResponse author) { this.author = author; }
    public Long getConferenceId() { return conferenceId; }
    public void setConferenceId(Long conferenceId) { this.conferenceId = conferenceId; }
    public String getConferenceTitle() { return conferenceTitle; }
    public void setConferenceTitle(String conferenceTitle) { this.conferenceTitle = conferenceTitle; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAbstractText() { return abstractText; }
    public void setAbstractText(String abstractText) { this.abstractText = abstractText; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getKeywords() { return keywords; }
    public void setKeywords(String keywords) { this.keywords = keywords; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public PaperStatus getStatus() { return status; }
    public void setStatus(PaperStatus status) { this.status = status; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
    public Double getAverageScore() { return averageScore; }
    public void setAverageScore(Double averageScore) { this.averageScore = averageScore; }
    public Integer getReviewCount() { return reviewCount; }
    public void setReviewCount(Integer reviewCount) { this.reviewCount = reviewCount; }
}

