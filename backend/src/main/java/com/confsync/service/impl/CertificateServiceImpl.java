package com.confsync.service.impl;

import com.confsync.dto.CertificateResponse;
import com.confsync.entity.Certificate;
import com.confsync.entity.Conference;
import com.confsync.entity.Registration;
import com.confsync.entity.User;
import com.confsync.enums.RegistrationStatus;
import com.confsync.exception.BadRequestException;
import com.confsync.exception.FileStorageException;
import com.confsync.exception.ResourceNotFoundException;
import com.confsync.repository.CertificateRepository;
import com.confsync.repository.RegistrationRepository;
import com.confsync.service.CertificateService;
import com.confsync.service.ConferenceService;
import com.confsync.service.NotificationService;
import com.confsync.service.UserService;
import com.confsync.util.CertificatePdfGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class CertificateServiceImpl implements CertificateService {

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private ConferenceService conferenceService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CertificatePdfGenerator pdfGenerator;

    @Value("${file.upload-certificates-dir:uploads/certificates}")
    private String certificatesDir;

    @Override
    public CertificateResponse generateCertificate(Long conferenceId, Long participantId) {
        Conference conference = conferenceService.getConferenceEntityById(conferenceId);
        User participant = userService.getUserById(participantId);

        // Check if certificate already generated
        var existing = certificateRepository.findByParticipantIdAndConferenceId(participant.getId(), conference.getId());
        if (existing.isPresent()) {
            return CertificateResponse.fromEntity(existing.get());
        }

        // Validate registration
        Registration reg = registrationRepository.findByParticipantIdAndConferenceId(participant.getId(), conference.getId())
                .orElseThrow(() -> new BadRequestException("Participant is not registered for this conference"));

        if (reg.getStatus() == RegistrationStatus.CANCELLED) {
            throw new BadRequestException("Cannot generate certificate for a cancelled registration");
        }

        if (!Boolean.TRUE.equals(reg.getAttendanceStatus())) {
            throw new BadRequestException("Cannot generate certificate: Participant has not verified conference attendance");
        }

        String certNum = "CS-CERT-" + conference.getId() + "-" + participant.getId() + "-" + System.currentTimeMillis();
        String verifyCode = UUID.randomUUID().toString().substring(0, 12).toUpperCase();

        String filename = "Certificate_" + certNum + ".pdf";
        File targetFile = Paths.get(certificatesDir, filename).toFile();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        String datesStr = conference.getStartDate().format(dtf) + " - " + conference.getEndDate().format(dtf);

        try {
            pdfGenerator.generateCertificatePdf(
                    targetFile,
                    participant.getName(),
                    conference.getTitle(),
                    datesStr,
                    certNum,
                    verifyCode
            );
        } catch (Exception e) {
            throw new FileStorageException("Could not generate certificate PDF: " + e.getMessage(), e);
        }

        Certificate certificate = new Certificate(participant, conference, certNum, verifyCode, targetFile.getAbsolutePath());
        Certificate saved = certificateRepository.save(certificate);

        notificationService.sendNotification(participant,
                "Certificate Generated!",
                "Your verified Certificate of Participation for " + conference.getTitle() + " is ready for download.");

        return CertificateResponse.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificateResponse> getMyCertificates() {
        User currentUser = userService.getCurrentUserEntity();
        return certificateRepository.findByParticipantId(currentUser.getId()).stream()
                .map(CertificateResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificateResponse> getAllCertificates() {
        return certificateRepository.findAll().stream()
                .map(CertificateResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CertificateResponse verifyCertificate(String verificationCode) {
        Certificate cert = certificateRepository.findByVerificationCode(verificationCode.trim())
                .orElseThrow(() -> new ResourceNotFoundException("Certificate not found with verification code: " + verificationCode));
        return CertificateResponse.fromEntity(cert);
    }

    @Override
    @Transactional(readOnly = true)
    public Resource downloadCertificatePdf(Long certificateId) {
        Certificate cert = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate", "id", certificateId));

        try {
            Path path = Paths.get(cert.getFilePath()).normalize();
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new FileStorageException("Certificate PDF file could not be read: " + cert.getFilePath());
            }
        } catch (MalformedURLException e) {
            throw new FileStorageException("Malformed certificate path: " + cert.getFilePath(), e);
        }
    }
}

