package com.confsync.service;

import com.confsync.dto.SessionRequest;
import com.confsync.dto.SessionResponse;

import java.util.List;

public interface SessionService {
    SessionResponse createSession(SessionRequest request);
    SessionResponse updateSession(Long id, SessionRequest request);
    void deleteSession(Long id);
    SessionResponse getSessionById(Long id);
    List<SessionResponse> getAllSessions();
    List<SessionResponse> getSessionsByConferenceId(Long conferenceId);
    List<SessionResponse> getSessionsBySpeakerId(Long speakerId);
}

