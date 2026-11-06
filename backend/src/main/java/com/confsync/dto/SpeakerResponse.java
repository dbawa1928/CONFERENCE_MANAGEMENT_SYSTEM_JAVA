package com.confsync.dto;

import com.confsync.entity.Speaker;

public class SpeakerResponse {
    private Long id;
    private UserResponse user;
    private String organization;
    private String designation;
    private String biography;
    private String expertise;
    private String phone;

    public SpeakerResponse() {}

    public static SpeakerResponse fromEntity(Speaker spk) {
        if (spk == null) return null;
        SpeakerResponse resp = new SpeakerResponse();
        resp.setId(spk.getId());
        resp.setUser(UserResponse.fromEntity(spk.getUser()));
        resp.setOrganization(spk.getOrganization());
        resp.setDesignation(spk.getDesignation());
        resp.setBiography(spk.getBiography());
        resp.setExpertise(spk.getExpertise());
        resp.setPhone(spk.getPhone());
        return resp;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public UserResponse getUser() { return user; }
    public void setUser(UserResponse user) { this.user = user; }
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

