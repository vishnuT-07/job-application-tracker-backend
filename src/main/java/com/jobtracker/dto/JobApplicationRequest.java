package com.jobtracker.dto;

import com.jobtracker.enums.ApplicationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Request payload for creating or updating a Job Application.
 * CSE Viva Note: Using DTOs separates API request validation from database entities.
 */
public class JobApplicationRequest {

    @NotBlank(message = "Company name cannot be blank")
    private String companyName;

    @NotBlank(message = "Job title cannot be blank")
    private String jobTitle;

    @NotNull(message = "Application date is required")
    private LocalDate applicationDate;

    private String jobLink;

    private String notes;

    private ApplicationStatus status = ApplicationStatus.APPLIED;

    public JobApplicationRequest() {
    }

    public JobApplicationRequest(String companyName, String jobTitle, LocalDate applicationDate, String jobLink, String notes, ApplicationStatus status) {
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.applicationDate = applicationDate;
        this.jobLink = jobLink;
        this.notes = notes;
        this.status = status != null ? status : ApplicationStatus.APPLIED;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getJobLink() {
        return jobLink;
    }

    public void setJobLink(String jobLink) {
        this.jobLink = jobLink;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
}
