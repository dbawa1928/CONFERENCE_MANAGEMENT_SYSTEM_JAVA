package com.confsync.service;

import com.confsync.dto.PaperRequest;
import com.confsync.dto.PaperResponse;
import com.confsync.entity.Paper;
import com.confsync.enums.PaperStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PaperService {
    PaperResponse submitPaper(PaperRequest request, MultipartFile file);
    PaperResponse getPaperById(Long id);
    Paper getPaperEntityById(Long id);
    List<PaperResponse> getAllPapers();
    List<PaperResponse> getMyPapers();
    List<PaperResponse> getPapersByConferenceId(Long conferenceId);
    List<PaperResponse> getPapersByStatus(PaperStatus status);
    PaperResponse updatePaperStatus(Long id, PaperStatus status);
}

