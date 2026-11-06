package com.confsync.service.impl;

import com.confsync.dto.PaperRequest;
import com.confsync.dto.PaperResponse;
import com.confsync.entity.Conference;
import com.confsync.entity.Paper;
import com.confsync.entity.Review;
import com.confsync.entity.User;
import com.confsync.enums.PaperStatus;
import com.confsync.exception.ResourceNotFoundException;
import com.confsync.repository.PaperRepository;
import com.confsync.repository.ReviewRepository;
import com.confsync.service.ConferenceService;
import com.confsync.service.FileStorageService;
import com.confsync.service.NotificationService;
import com.confsync.service.PaperService;
import com.confsync.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaperServiceImpl implements PaperService {

    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ConferenceService conferenceService;

    @Autowired
    private UserService userService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private NotificationService notificationService;

    private PaperResponse enrichPaperResponse(Paper paper) {
        PaperResponse resp = PaperResponse.fromEntity(paper);
        List<Review> reviews = reviewRepository.findByPaperId(paper.getId());
        resp.setReviewCount(reviews.size());

        if (!reviews.isEmpty()) {
            double avg = reviews.stream()
                    .mapToDouble(Review::getOverallScore)
                    .average()
                    .orElse(0.0);
            resp.setAverageScore(Math.round(avg * 100.0) / 100.0);
        } else {
            resp.setAverageScore(null);
        }
        return resp;
    }

    @Override
    public PaperResponse submitPaper(PaperRequest request, MultipartFile file) {
        User currentUser = userService.getCurrentUserEntity();
        Conference conference = conferenceService.getConferenceEntityById(request.getConferenceId());

        String savedFilePath = null;
        if (file != null && !file.isEmpty()) {
            savedFilePath = fileStorageService.storePaperFile(file);
        }

        Paper paper = new Paper(
                currentUser,
                conference,
                request.getTitle().trim(),
                request.getAbstractText().trim(),
                request.getCategory().trim(),
                request.getKeywords().trim(),
                savedFilePath
        );

        Paper saved = paperRepository.save(paper);

        // Notify author
        notificationService.sendNotification(currentUser,
                "Research Paper Submitted",
                "Your paper '" + saved.getTitle() + "' was successfully submitted to " + conference.getTitle() + ".");

        return enrichPaperResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PaperResponse getPaperById(Long id) {
        Paper paper = getPaperEntityById(id);
        return enrichPaperResponse(paper);
    }

    @Override
    @Transactional(readOnly = true)
    public Paper getPaperEntityById(Long id) {
        return paperRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paper", "id", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaperResponse> getAllPapers() {
        return paperRepository.findAll().stream()
                .map(this::enrichPaperResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaperResponse> getMyPapers() {
        User currentUser = userService.getCurrentUserEntity();
        return paperRepository.findByAuthorId(currentUser.getId()).stream()
                .map(this::enrichPaperResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaperResponse> getPapersByConferenceId(Long conferenceId) {
        return paperRepository.findByConferenceId(conferenceId).stream()
                .map(this::enrichPaperResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaperResponse> getPapersByStatus(PaperStatus status) {
        return paperRepository.findByStatus(status).stream()
                .map(this::enrichPaperResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PaperResponse updatePaperStatus(Long id, PaperStatus status) {
        Paper paper = getPaperEntityById(id);
        paper.setStatus(status);
        Paper updated = paperRepository.save(paper);

        String message = "Your research paper '" + paper.getTitle() + "' status has been updated to: " + status.name();
        notificationService.sendNotification(paper.getAuthor(), "Paper Status Updated", message);

        return enrichPaperResponse(updated);
    }
}

