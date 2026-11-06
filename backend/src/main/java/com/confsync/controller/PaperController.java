package com.confsync.controller;

import com.confsync.dto.ApiResponse;
import com.confsync.dto.PaperRequest;
import com.confsync.dto.PaperResponse;
import com.confsync.dto.PaperStatusUpdateRequest;
import com.confsync.entity.Paper;
import com.confsync.enums.PaperStatus;
import com.confsync.service.FileStorageService;
import com.confsync.service.PaperService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/papers")
public class PaperController {

    @Autowired
    private PaperService paperService;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @PreAuthorize("hasRole('AUTHOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PaperResponse>> submitPaper(
            @RequestParam("conferenceId") Long conferenceId,
            @RequestParam("title") String title,
            @RequestParam("abstractText") String abstractText,
            @RequestParam("category") String category,
            @RequestParam("keywords") String keywords,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        PaperRequest request = new PaperRequest();
        request.setConferenceId(conferenceId);
        request.setTitle(title);
        request.setAbstractText(abstractText);
        request.setCategory(category);
        request.setKeywords(keywords);

        PaperResponse response = paperService.submitPaper(request, file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Research paper submitted successfully", response));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<PaperResponse>>> getAllPapers(
            @RequestParam(required = false) PaperStatus status) {
        List<PaperResponse> papers = (status != null) ?
                paperService.getPapersByStatus(status) :
                paperService.getAllPapers();
        return ResponseEntity.ok(ApiResponse.success("Research papers retrieved", papers));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('AUTHOR') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<PaperResponse>>> getMyPapers() {
        List<PaperResponse> papers = paperService.getMyPapers();
        return ResponseEntity.ok(ApiResponse.success("Your submitted papers", papers));
    }

    @GetMapping("/conference/{conferenceId}")
    public ResponseEntity<ApiResponse<List<PaperResponse>>> getPapersByConference(@PathVariable Long conferenceId) {
        List<PaperResponse> papers = paperService.getPapersByConferenceId(conferenceId);
        return ResponseEntity.ok(ApiResponse.success("Conference papers retrieved", papers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaperResponse>> getPaperById(@PathVariable Long id) {
        PaperResponse response = paperService.getPaperById(id);
        return ResponseEntity.ok(ApiResponse.success("Paper details retrieved", response));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PaperResponse>> updatePaperStatus(
            @PathVariable Long id,
            @Valid @RequestBody PaperStatusUpdateRequest request) {
        PaperResponse response = paperService.updatePaperStatus(id, request.getStatus());
        return ResponseEntity.ok(ApiResponse.success("Paper status updated", response));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadPaperFile(@PathVariable Long id) {
        Paper paper = paperService.getPaperEntityById(id);
        if (paper.getFilePath() == null) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = fileStorageService.loadFileAsResource(paper.getFilePath());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}

