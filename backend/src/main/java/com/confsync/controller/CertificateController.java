package com.confsync.controller;

import com.confsync.dto.ApiResponse;
import com.confsync.dto.CertificateResponse;
import com.confsync.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<CertificateResponse>> generateCertificate(
            @RequestBody Map<String, Long> payload) {
        Long conferenceId = payload.get("conferenceId");
        Long participantId = payload.get("participantId");
        CertificateResponse response = certificateService.generateCertificate(conferenceId, participantId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Certificate generated successfully", response));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<CertificateResponse>>> getMyCertificates() {
        List<CertificateResponse> list = certificateService.getMyCertificates();
        return ResponseEntity.ok(ApiResponse.success("Your certificates", list));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<CertificateResponse>>> getAllCertificates() {
        List<CertificateResponse> list = certificateService.getAllCertificates();
        return ResponseEntity.ok(ApiResponse.success("All certificates retrieved", list));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadCertificatePdf(@PathVariable Long id) {
        Resource resource = certificateService.downloadCertificatePdf(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/verify/{verificationCode}")
    public ResponseEntity<ApiResponse<CertificateResponse>> verifyCertificate(
            @PathVariable String verificationCode) {
        CertificateResponse response = certificateService.verifyCertificate(verificationCode);
        return ResponseEntity.ok(ApiResponse.success("Certificate verified successfully", response));
    }
}

