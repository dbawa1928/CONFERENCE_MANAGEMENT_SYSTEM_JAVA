package com.confsync.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Speaker Entity containing professional profile and expertise details.
 */
@Entity
@Table(name = "speakers")
public class Speaker extends BaseEntity {

    @NotNull(message = "Associated user is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank(message = "Organization is required")
    @Column(name = "organization", nullable = false)
    private String organization;

    @NotBlank(message = "Designation is required")
    @Column(name = "designation", nullable = false)
    private String designation;

    @Lob
    @Column(name = "biography", columnDefinition = "TEXT")
    private String biography;

    @NotBlank(message = "Expertise is required")
    @Column(name = "expertise", nullable = false)
    private String expertise;

    @Column(name = "phone")
    private String phone;

    public Speaker() {
        super();
    }

    public Speaker(User user, String organization, String designation, String biography, String expertise, String phone) {
        this.user = user;
        this.organization = organization;
        this.designation = designation;
        this.biography = biography;
        this.expertise = expertise;
        this.phone = phone;
    }

    // Getters and Setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

