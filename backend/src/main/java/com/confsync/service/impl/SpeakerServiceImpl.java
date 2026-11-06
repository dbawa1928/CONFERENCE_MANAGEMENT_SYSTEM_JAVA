package com.confsync.service.impl;

import com.confsync.dto.SpeakerRequest;
import com.confsync.dto.SpeakerResponse;
import com.confsync.entity.Speaker;
import com.confsync.entity.User;
import com.confsync.enums.Role;
import com.confsync.exception.BadRequestException;
import com.confsync.exception.ResourceNotFoundException;
import com.confsync.repository.SpeakerRepository;
import com.confsync.service.SpeakerService;
import com.confsync.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SpeakerServiceImpl implements SpeakerService {

    @Autowired
    private SpeakerRepository speakerRepository;

    @Autowired
    private UserService userService;

    @Override
    public SpeakerResponse createSpeaker(SpeakerRequest request) {
        User user = userService.getUserById(request.getUserId());

        if (speakerRepository.existsByUserId(user.getId())) {
            throw new BadRequestException("A speaker profile already exists for user: " + user.getName());
        }

        // Ensure user has SPEAKER role
        if (user.getRole() != Role.SPEAKER && user.getRole() != Role.ADMIN) {
            userService.updateUserRole(user.getId(), Role.SPEAKER);
        }

        Speaker speaker = new Speaker(user, request.getOrganization().trim(),
                request.getDesignation().trim(), request.getBiography(),
                request.getExpertise().trim(), request.getPhone());

        Speaker saved = speakerRepository.save(speaker);
        return SpeakerResponse.fromEntity(saved);
    }

    @Override
    public SpeakerResponse updateSpeaker(Long id, SpeakerRequest request) {
        Speaker speaker = getSpeakerEntityById(id);
        speaker.setOrganization(request.getOrganization().trim());
        speaker.setDesignation(request.getDesignation().trim());
        speaker.setBiography(request.getBiography());
        speaker.setExpertise(request.getExpertise().trim());
        speaker.setPhone(request.getPhone());

        Speaker updated = speakerRepository.save(speaker);
        return SpeakerResponse.fromEntity(updated);
    }

    @Override
    public void deleteSpeaker(Long id) {
        Speaker speaker = getSpeakerEntityById(id);
        speakerRepository.delete(speaker);
    }

    @Override
    @Transactional(readOnly = true)
    public SpeakerResponse getSpeakerById(Long id) {
        return SpeakerResponse.fromEntity(getSpeakerEntityById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Speaker getSpeakerEntityById(Long id) {
        return speakerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Speaker", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public SpeakerResponse getSpeakerByUserId(Long userId) {
        Speaker speaker = speakerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Speaker profile", "userId", userId));
        return SpeakerResponse.fromEntity(speaker);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpeakerResponse> getAllSpeakers() {
        return speakerRepository.findAll().stream()
                .map(SpeakerResponse::fromEntity)
                .collect(Collectors.toList());
    }
}

