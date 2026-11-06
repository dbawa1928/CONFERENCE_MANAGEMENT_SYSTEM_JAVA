package com.confsync.dto;

import jakarta.validation.constraints.NotBlank;

public class AttendanceCheckInRequest {
    @NotBlank(message = "QR Code or Registration Code is required")
    private String qrCode;

    public AttendanceCheckInRequest() {}

    public AttendanceCheckInRequest(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }
}

