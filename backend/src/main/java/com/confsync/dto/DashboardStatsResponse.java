package com.confsync.dto;

import java.util.Map;

/**
 * Aggregated analytics and stats demonstrating Java Collections (Map, List).
 */
public class DashboardStatsResponse {
    private long totalUsers;
    private long totalConferences;
    private long totalParticipants;
    private long totalSpeakers;
    private long totalPapers;
    private long acceptedPapers;
    private long pendingReviews;
    private long totalAttendance;
    private Map<String, Long> papersByStatus;
    private Map<String, Long> conferencesByCategory;
    private Map<String, Long> usersByRole;

    public DashboardStatsResponse() {}

    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
    public long getTotalConferences() { return totalConferences; }
    public void setTotalConferences(long totalConferences) { this.totalConferences = totalConferences; }
    public long getTotalParticipants() { return totalParticipants; }
    public void setTotalParticipants(long totalParticipants) { this.totalParticipants = totalParticipants; }
    public long getTotalSpeakers() { return totalSpeakers; }
    public void setTotalSpeakers(long totalSpeakers) { this.totalSpeakers = totalSpeakers; }
    public long getTotalPapers() { return totalPapers; }
    public void setTotalPapers(long totalPapers) { this.totalPapers = totalPapers; }
    public long getAcceptedPapers() { return acceptedPapers; }
    public void setAcceptedPapers(long acceptedPapers) { this.acceptedPapers = acceptedPapers; }
    public long getPendingReviews() { return pendingReviews; }
    public void setPendingReviews(long pendingReviews) { this.pendingReviews = pendingReviews; }
    public long getTotalAttendance() { return totalAttendance; }
    public void setTotalAttendance(long totalAttendance) { this.totalAttendance = totalAttendance; }
    public Map<String, Long> getPapersByStatus() { return papersByStatus; }
    public void setPapersByStatus(Map<String, Long> papersByStatus) { this.papersByStatus = papersByStatus; }
    public Map<String, Long> getConferencesByCategory() { return conferencesByCategory; }
    public void setConferencesByCategory(Map<String, Long> conferencesByCategory) { this.conferencesByCategory = conferencesByCategory; }
    public Map<String, Long> getUsersByRole() { return usersByRole; }
    public void setUsersByRole(Map<String, Long> usersByRole) { this.usersByRole = usersByRole; }
}

