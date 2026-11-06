package com.confsync.dto;

import com.confsync.entity.Speaker;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SpeakerRequest {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Organization is required")
    private String organization;

    @NotBlank(message = "Designation is required")
    private String designation;

    private String biography;

    @NotBlank(message = "Expertise is required")
    private String expertise;

    private String phone;

    public SpeakerRequest() {}

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getOrganization() { return organization; }
    public void setOrganization(String organization) { this.organization = organization; }
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }
    public String getBiography() { return biography; }
    public void setBiography(String biography) { this.biography = biography; }
    public String getExpertise() { return expertise; }
    public void setExpertise(String expertise) { this.expertise = expertise; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}

