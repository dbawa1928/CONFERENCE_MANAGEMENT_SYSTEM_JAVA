package com.confsync.service.impl;

import com.confsync.exception.FileStorageException;
import com.confsync.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.UUID;

/**
 * Service demonstrating Java NIO File Handling for storing research papers and assets locally.
 */
@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path papersLocation;

    public FileStorageServiceImpl(@Value("${file.upload-papers-dir:uploads/papers}") String papersDir) {
        this.papersLocation = Paths.get(papersDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.papersLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create papers upload directory", ex);
        }
    }

    @Override
    public String storePaperFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename() != null ? file.getOriginalFilename() : "paper.pdf");

        // Validate clean filename
        if (originalFilename.contains("..")) {
            throw new FileStorageException("Filename contains invalid path sequence: " + originalFilename);
        }

        String fileExtension = "";
        int extIndex = originalFilename.lastIndexOf('.');
        if (extIndex > 0) {
            fileExtension = originalFilename.substring(extIndex);
        }

        String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFilename;

        try {
            Path targetLocation = this.papersLocation.resolve(uniqueFileName);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
            }
            return targetLocation.toString();
        } catch (IOException ex) {
            throw new FileStorageException("Failed to store paper file " + originalFilename, ex);
        }
    }

    @Override
    public Resource loadFileAsResource(String filePath) {
        try {
            Path path = Paths.get(filePath).normalize();
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new FileStorageException("File not found or unreadable: " + filePath);
            }
        } catch (MalformedURLException ex) {
            throw new FileStorageException("Malformed file path: " + filePath, ex);
        }
    }

    @Override
    public void deleteFile(String filePath) {
        if (filePath != null) {
            try {
                Path path = Paths.get(filePath).normalize();
                Files.deleteIfExists(path);
            } catch (IOException e) {
                // Ignore cleanup failures
            }
        }
    }
}

