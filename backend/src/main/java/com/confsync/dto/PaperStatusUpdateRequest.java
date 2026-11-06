package com.confsync.dto;

import com.confsync.enums.PaperStatus;
import jakarta.validation.constraints.NotNull;

public class PaperStatusUpdateRequest {
    @NotNull(message = "Status is required")
    private PaperStatus status;

    public PaperStatusUpdateRequest() {}

    public PaperStatusUpdateRequest(PaperStatus status) {
        this.status = status;
    }

    public PaperStatus getStatus() { return status; }
    public void setStatus(PaperStatus status) { this.status = status; }
}

