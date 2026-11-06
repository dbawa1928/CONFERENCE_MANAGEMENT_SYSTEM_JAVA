package com.confsync.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PaperRequest {
    @NotNull(message = "Conference ID is required")
    private Long conferenceId;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Abstract is required")
    private String abstractText;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Keywords are required")
    private String keywords;

    public PaperRequest() {}

    public Long getConferenceId() { return conferenceId; }
    public void setConferenceId(Long conferenceId) { this.conferenceId = conferenceId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAbstractText() { return abstractText; }
    public void setAbstractText(String abstractText) { this.abstractText = abstractText; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getKeywords() { return keywords; }
    public void setKeywords(String keywords) { this.keywords = keywords; }
}

