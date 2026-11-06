package com.confsync.service.impl;

import com.confsync.dto.DashboardStatsResponse;
import com.confsync.entity.Conference;
import com.confsync.entity.Paper;
import com.confsync.entity.User;
import com.confsync.enums.AssignmentStatus;
import com.confsync.enums.PaperStatus;
import com.confsync.enums.Role;
import com.confsync.repository.*;
import com.confsync.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service demonstrating Java Collections (Map, List, Set) and Stream APIs
 * for analytics aggregation and dashboard reporting.
 */
@Service
@Transactional(readOnly = true)
public class AnalyticsServiceImpl implements AnalyticsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private SpeakerRepository speakerRepository;

    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private ReviewerAssignmentRepository assignmentRepository;

    @Override
    public DashboardStatsResponse getAdminDashboardStats() {
        DashboardStatsResponse stats = new DashboardStatsResponse();

        stats.setTotalUsers(userRepository.count());
        stats.setTotalConferences(conferenceRepository.count());
        stats.setTotalParticipants(userRepository.countByRole(Role.PARTICIPANT));
        stats.setTotalSpeakers(speakerRepository.count());
        stats.setTotalPapers(paperRepository.count());
        stats.setAcceptedPapers(paperRepository.countByStatus(PaperStatus.ACCEPTED));
        stats.setPendingReviews(assignmentRepository.findAll().stream()
                .filter(a -> a.getStatus() == AssignmentStatus.PENDING)
                .count());
        stats.setTotalAttendance(registrationRepository.countByAttendanceStatus(true));

        // Grouping papers by status using Java Streams and Map Collection
        List<Paper> allPapers = paperRepository.findAll();
        Map<String, Long> papersByStatus = allPapers.stream()
                .collect(Collectors.groupingBy(p -> p.getStatus().name(), Collectors.counting()));
        stats.setPapersByStatus(papersByStatus);

        // Grouping conferences by category using Java Streams and Map Collection
        List<Conference> allConferences = conferenceRepository.findAll();
        Map<String, Long> conferencesByCategory = allConferences.stream()
                .collect(Collectors.groupingBy(Conference::getCategory, Collectors.counting()));
        stats.setConferencesByCategory(conferencesByCategory);

        // Grouping users by role using Java Streams and Map Collection
        List<User> allUsers = userRepository.findAll();
        Map<String, Long> usersByRole = allUsers.stream()
                .collect(Collectors.groupingBy(u -> u.getRole().name(), Collectors.counting()));
        stats.setUsersByRole(usersByRole);

        return stats;
    }
}

