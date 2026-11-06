package com.confsync.entity;

import com.confsync.enums.PaperStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Paper Entity representing an academic research paper submitted by an Author.
 */
@Entity
@Table(name = "papers")
public class Paper extends BaseEntity {

    @NotNull(message = "Author is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @NotNull(message = "Conference is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "conference_id", nullable = false)
    private Conference conference;

    @NotBlank(message = "Title is required")
    @Column(name = "title", nullable = false)
    private String title;

    @NotBlank(message = "Abstract is required")
    @Lob
    @Column(name = "abstract", columnDefinition = "TEXT", nullable = false)
    private String abstractText;

    @NotBlank(message = "Category is required")
    @Column(name = "category", nullable = false)
    private String category;

    @NotBlank(message = "Keywords are required")
    @Column(name = "keywords", nullable = false)
    private String keywords;

    @Column(name = "file_path")
    private String filePath;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private PaperStatus status;

    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;

    public Paper() {
        super();
        this.status = PaperStatus.SUBMITTED;
        this.submittedAt = LocalDateTime.now();
    }

    public Paper(User author, Conference conference, String title, String abstractText,
                 String category, String keywords, String filePath) {
        this();
        this.author = author;
        this.conference = conference;
        this.title = title;
        this.abstractText = abstractText;
        this.category = category;
        this.keywords = keywords;
        this.filePath = filePath;
    }

    // Getters and Setters
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public PaperStatus getStatus() {
        return status;
    }

    public void setStatus(PaperStatus status) {
        this.status = status;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
}

