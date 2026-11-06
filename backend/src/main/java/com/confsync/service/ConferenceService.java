package com.confsync.service;

import com.confsync.dto.ConferenceRequest;
import com.confsync.dto.ConferenceResponse;
import com.confsync.entity.Conference;
import com.confsync.enums.ConferenceStatus;

import java.util.List;

public interface ConferenceService {
    ConferenceResponse createConference(ConferenceRequest request);
    ConferenceResponse updateConference(Long id, ConferenceRequest request);
    void deleteConference(Long id);
    ConferenceResponse getConferenceById(Long id);
    Conference getConferenceEntityById(Long id);
    List<ConferenceResponse> getAllConferences();
    List<ConferenceResponse> getConferencesByStatus(ConferenceStatus status);
    List<ConferenceResponse> getUpcomingConferences();
    List<ConferenceResponse> searchConferences(String query, String category, ConferenceStatus status);
}

