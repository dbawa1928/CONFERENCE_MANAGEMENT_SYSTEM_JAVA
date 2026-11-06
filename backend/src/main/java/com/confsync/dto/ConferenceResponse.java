package com.confsync.dto;

import com.confsync.entity.Conference;
import com.confsync.enums.ConferenceStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ConferenceResponse {
    private Long id;
    private String title;
    private String description;
    private String category;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate registrationDeadline;
    private String venue;
    private Integer maxParticipants;
    private Long registeredCount;
    private Integer remainingCapacity;
    private ConferenceStatus status;
    private LocalDateTime createdAt;

    public ConferenceResponse() {}

    public static ConferenceResponse fromEntity(Conference conf, long registeredCount) {
        if (conf == null) return null;
        ConferenceResponse resp = new ConferenceResponse();
        resp.setId(conf.getId());
        resp.setTitle(conf.getTitle());
        resp.setDescription(conf.getDescription());
        resp.setCategory(conf.getCategory());
        resp.setStartDate(conf.getStartDate());
        resp.setEndDate(conf.getEndDate());
        resp.setRegistrationDeadline(conf.getRegistrationDeadline());
        resp.setVenue(conf.getVenue());
        resp.setMaxParticipants(conf.getMaxParticipants());
        resp.setRegisteredCount(registeredCount);
        resp.setRemainingCapacity(Math.max(0, conf.getMaxParticipants() - (int) registeredCount));
        resp.setStatus(conf.getStatus());
        resp.setCreatedAt(conf.getCreatedAt());
        return resp;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public LocalDate getRegistrationDeadline() { return registrationDeadline; }
    public void setRegistrationDeadline(LocalDate registrationDeadline) { this.registrationDeadline = registrationDeadline; }
    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
    public Integer getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(Integer maxParticipants) { this.maxParticipants = maxParticipants; }
    public Long getRegisteredCount() { return registeredCount; }
    public void setRegisteredCount(Long registeredCount) { this.registeredCount = registeredCount; }
    public Integer getRemainingCapacity() { return remainingCapacity; }
    public void setRemainingCapacity(Integer remainingCapacity) { this.remainingCapacity = remainingCapacity; }
    public ConferenceStatus getStatus() { return status; }
    public void setStatus(ConferenceStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

