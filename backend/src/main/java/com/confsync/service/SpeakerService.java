package com.confsync.service;

import com.confsync.dto.SpeakerRequest;
import com.confsync.dto.SpeakerResponse;
import com.confsync.entity.Speaker;

import java.util.List;

public interface SpeakerService {
    SpeakerResponse createSpeaker(SpeakerRequest request);
    SpeakerResponse updateSpeaker(Long id, SpeakerRequest request);
    void deleteSpeaker(Long id);
    SpeakerResponse getSpeakerById(Long id);
    Speaker getSpeakerEntityById(Long id);
    SpeakerResponse getSpeakerByUserId(Long userId);
    List<SpeakerResponse> getAllSpeakers();
}

