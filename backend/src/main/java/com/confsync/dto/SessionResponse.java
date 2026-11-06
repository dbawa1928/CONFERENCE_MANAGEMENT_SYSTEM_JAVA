package com.confsync.dto;

import com.confsync.entity.Session;
import java.time.LocalDate;
import java.time.LocalTime;

public class SessionResponse {
    private Long id;
    private Long conferenceId;
    private String conferenceTitle;
    private SpeakerResponse speaker;
    private String title;
    private String description;
    private LocalDate sessionDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String hall;
    private String category;
    private Integer maxCapacity;

    public SessionResponse() {}

    public static SessionResponse fromEntity(Session s) {
        if (s == null) return null;
        SessionResponse resp = new SessionResponse();
        resp.setId(s.getId());
        resp.setConferenceId(s.getConference().getId());
        resp.setConferenceTitle(s.getConference().getTitle());
        if (s.getSpeaker() != null) {
            resp.setSpeaker(SpeakerResponse.fromEntity(s.getSpeaker()));
        }
        resp.setTitle(s.getTitle());
        resp.setDescription(s.getDescription());
        resp.setSessionDate(s.getSessionDate());
        resp.setStartTime(s.getStartTime());
        resp.setEndTime(s.getEndTime());
        resp.setHall(s.getHall());
        resp.setCategory(s.getCategory());
        resp.setMaxCapacity(s.getMaxCapacity());
        return resp;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getConferenceId() { return conferenceId; }
    public void setConferenceId(Long conferenceId) { this.conferenceId = conferenceId; }
    public String getConferenceTitle() { return conferenceTitle; }
    public void setConferenceTitle(String conferenceTitle) { this.conferenceTitle = conferenceTitle; }
    public SpeakerResponse getSpeaker() { return speaker; }
    public void setSpeaker(SpeakerResponse speaker) { this.speaker = speaker; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getSessionDate() { return sessionDate; }
    public void setSessionDate(LocalDate sessionDate) { this.sessionDate = sessionDate; }
    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }
    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    public String getHall() { return hall; }
    public void setHall(String hall) { this.hall = hall; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Integer getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(Integer maxCapacity) { this.maxCapacity = maxCapacity; }
}

