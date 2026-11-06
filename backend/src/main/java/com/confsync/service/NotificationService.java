package com.confsync.service;

import com.confsync.dto.NotificationResponse;
import com.confsync.entity.User;

import java.util.List;

public interface NotificationService {
    NotificationResponse sendNotification(User recipient, String title, String message);
    List<NotificationResponse> getMyNotifications();
    void markAsRead(Long notificationId);
    void markAllAsRead();
}

