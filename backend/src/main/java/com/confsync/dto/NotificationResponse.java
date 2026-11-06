package com.confsync.dto;

import com.confsync.entity.Notification;
import java.time.LocalDateTime;

public class NotificationResponse {
    private Long id;
    private Long userId;
    private String title;
    private String message;
    private Boolean isRead;
    private LocalDateTime createdAt;

    public NotificationResponse() {}

    public static NotificationResponse fromEntity(Notification n) {
        if (n == null) return null;
        NotificationResponse resp = new NotificationResponse();
        resp.setId(n.getId());
        resp.setUserId(n.getUser().getId());
        resp.setTitle(n.getTitle());
        resp.setMessage(n.getMessage());
        resp.setIsRead(n.getIsRead());
        resp.setCreatedAt(n.getCreatedAt());
        return resp;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

