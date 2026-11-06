package com.confsync.service.impl;

import com.confsync.dto.SessionRequest;
import com.confsync.dto.SessionResponse;
import com.confsync.entity.Conference;
import com.confsync.entity.Session;
import com.confsync.entity.Speaker;
import com.confsync.exception.InvalidTimeRangeException;
import com.confsync.exception.ResourceNotFoundException;
import com.confsync.repository.SessionRepository;
import com.confsync.service.ConferenceService;
import com.confsync.service.SessionService;
import com.confsync.service.SpeakerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SessionServiceImpl implements SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private ConferenceService conferenceService;

    @Autowired
    private SpeakerService speakerService;

    private void validateTimes(SessionRequest req) {
        if (req.getEndTime().isBefore(req.getStartTime()) || req.getEndTime().equals(req.getStartTime())) {
            throw new InvalidTimeRangeException("Session end time must be after start time");
        }
    }

    @Override
    public SessionResponse createSession(SessionRequest request) {
        validateTimes(request);

        Conference conference = conferenceService.getConferenceEntityById(request.getConferenceId());
        Speaker speaker = null;
        if (request.getSpeakerId() != null) {
            speaker = speakerService.getSpeakerEntityById(request.getSpeakerId());
        }

        Session session = new Session(
                conference,
                speaker,
                request.getTitle().trim(),
                request.getDescription(),
                request.getSessionDate(),
                request.getStartTime(),
                request.getEndTime(),
                request.getHall().trim(),
                request.getCategory(),
                request.getMaxCapacity()
        );

        Session saved = sessionRepository.save(session);
        return SessionResponse.fromEntity(saved);
    }

    @Override
    public SessionResponse updateSession(Long id, SessionRequest request) {
        validateTimes(request);

        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session", "id", id));

        Conference conference = conferenceService.getConferenceEntityById(request.getConferenceId());
        Speaker speaker = null;
        if (request.getSpeakerId() != null) {
            speaker = speakerService.getSpeakerEntityById(request.getSpeakerId());
        }

        session.setConference(conference);
        session.setSpeaker(speaker);
        session.setTitle(request.getTitle().trim());
        session.setDescription(request.getDescription());
        session.setSessionDate(request.getSessionDate());
        session.setStartTime(request.getStartTime());
        session.setEndTime(request.getEndTime());
        session.setHall(request.getHall().trim());
        session.setCategory(request.getCategory());
        session.setMaxCapacity(request.getMaxCapacity());

        Session updated = sessionRepository.save(session);
        return SessionResponse.fromEntity(updated);
    }

    @Override
    public void deleteSession(Long id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session", "id", id));
        sessionRepository.delete(session);
    }

    @Override
    @Transactional(readOnly = true)
    public SessionResponse getSessionById(Long id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session", "id", id));
        return SessionResponse.fromEntity(session);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionResponse> getAllSessions() {
        return sessionRepository.findAll().stream()
                .map(SessionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionResponse> getSessionsByConferenceId(Long conferenceId) {
        return sessionRepository.findByConferenceIdOrderBySessionDateAscStartTimeAsc(conferenceId).stream()
                .map(SessionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionResponse> getSessionsBySpeakerId(Long speakerId) {
        return sessionRepository.findBySpeakerId(speakerId).stream()
                .map(SessionResponse::fromEntity)
                .collect(Collectors.toList());
    }
}

