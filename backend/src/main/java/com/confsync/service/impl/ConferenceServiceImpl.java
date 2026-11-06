package com.confsync.service.impl;

import com.confsync.dto.ConferenceRequest;
import com.confsync.dto.ConferenceResponse;
import com.confsync.entity.Conference;
import com.confsync.enums.ConferenceStatus;
import com.confsync.enums.RegistrationStatus;
import com.confsync.exception.InvalidTimeRangeException;
import com.confsync.exception.ResourceNotFoundException;
import com.confsync.repository.ConferenceRepository;
import com.confsync.repository.RegistrationRepository;
import com.confsync.service.ConferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ConferenceServiceImpl implements ConferenceService {

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    private void validateDates(ConferenceRequest req) {
        if (req.getEndDate().isBefore(req.getStartDate())) {
            throw new InvalidTimeRangeException("Conference end date cannot be before start date");
        }
        if (req.getRegistrationDeadline().isAfter(req.getStartDate())) {
            throw new InvalidTimeRangeException("Registration deadline cannot be after conference start date");
        }
    }

    private ConferenceResponse mapToResponse(Conference conf) {
        long regCount = registrationRepository.countByConferenceIdAndStatus(conf.getId(), RegistrationStatus.REGISTERED);
        return ConferenceResponse.fromEntity(conf, regCount);
    }

    @Override
    public ConferenceResponse createConference(ConferenceRequest request) {
        validateDates(request);

        Conference conf = new Conference();
        conf.setTitle(request.getTitle().trim());
        conf.setDescription(request.getDescription());
        conf.setCategory(request.getCategory().trim());
        conf.setStartDate(request.getStartDate());
        conf.setEndDate(request.getEndDate());
        conf.setRegistrationDeadline(request.getRegistrationDeadline());
        conf.setVenue(request.getVenue().trim());
        conf.setMaxParticipants(request.getMaxParticipants());
        conf.setStatus(request.getStatus() != null ? request.getStatus() : ConferenceStatus.UPCOMING);

        Conference saved = conferenceRepository.save(conf);
        return mapToResponse(saved);
    }

    @Override
    public ConferenceResponse updateConference(Long id, ConferenceRequest request) {
        validateDates(request);

        Conference conf = getConferenceEntityById(id);
        conf.setTitle(request.getTitle().trim());
        conf.setDescription(request.getDescription());
        conf.setCategory(request.getCategory().trim());
        conf.setStartDate(request.getStartDate());
        conf.setEndDate(request.getEndDate());
        conf.setRegistrationDeadline(request.getRegistrationDeadline());
        conf.setVenue(request.getVenue().trim());
        conf.setMaxParticipants(request.getMaxParticipants());
        if (request.getStatus() != null) {
            conf.setStatus(request.getStatus());
        }

        Conference updated = conferenceRepository.save(conf);
        return mapToResponse(updated);
    }

    @Override
    public void deleteConference(Long id) {
        Conference conf = getConferenceEntityById(id);
        conferenceRepository.delete(conf);
    }

    @Override
    @Transactional(readOnly = true)
    public ConferenceResponse getConferenceById(Long id) {
        Conference conf = getConferenceEntityById(id);
        return mapToResponse(conf);
    }

    @Override
    @Transactional(readOnly = true)
    public Conference getConferenceEntityById(Long id) {
        return conferenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conference", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConferenceResponse> getAllConferences() {
        return conferenceRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConferenceResponse> getConferencesByStatus(ConferenceStatus status) {
        return conferenceRepository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConferenceResponse> getUpcomingConferences() {
        return conferenceRepository.findByStartDateGreaterThanEqualOrderByStartDateAsc(LocalDate.now()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConferenceResponse> searchConferences(String query, String category, ConferenceStatus status) {
        String cleanQuery = (query != null && !query.trim().isEmpty()) ? query.trim().toLowerCase() : null;
        String cleanCategory = (category != null && !category.trim().isEmpty()) ? category.trim() : null;

        return conferenceRepository.findAll().stream()
                .filter(c -> {
                    if (cleanQuery != null) {
                        boolean matchesTitle = c.getTitle() != null && c.getTitle().toLowerCase().contains(cleanQuery);
                        boolean matchesDesc = c.getDescription() != null && c.getDescription().toLowerCase().contains(cleanQuery);
                        boolean matchesVenue = c.getVenue() != null && c.getVenue().toLowerCase().contains(cleanQuery);
                        if (!matchesTitle && !matchesDesc && !matchesVenue) {
                            return false;
                        }
                    }
                    if (cleanCategory != null && !cleanCategory.equalsIgnoreCase(c.getCategory())) {
                        return false;
                    }
                    if (status != null && c.getStatus() != status) {
                        return false;
                    }
                    return true;
                })
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}

