package com.confsync.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String storePaperFile(MultipartFile file);
    Resource loadFileAsResource(String filePath);
    void deleteFile(String filePath);
}

