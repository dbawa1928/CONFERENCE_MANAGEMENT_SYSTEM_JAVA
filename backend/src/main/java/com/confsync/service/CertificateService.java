package com.confsync.service;

import com.confsync.dto.CertificateResponse;
import org.springframework.core.io.Resource;

import java.util.List;

public interface CertificateService {
    CertificateResponse generateCertificate(Long conferenceId, Long participantId);
    List<CertificateResponse> getMyCertificates();
    List<CertificateResponse> getAllCertificates();
    CertificateResponse verifyCertificate(String verificationCode);
    Resource downloadCertificatePdf(Long certificateId);
}

