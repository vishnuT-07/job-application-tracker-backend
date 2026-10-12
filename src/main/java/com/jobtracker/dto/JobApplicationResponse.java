package com.jobtracker.dto;

import com.jobtracker.enums.ApplicationStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO returned by Application endpoints.
 * CSE Viva Note: Returning DTOs instead of raw Entities prevents circular serialization
 * loops (JSON infinite recursion) between Parent JobApplication and Child Interviews.
 */
public class JobApplicationResponse {

    private Long id;
    private String companyName;
    private String jobTitle;
    private LocalDate applicationDate;
    private String jobLink;
    private String notes;
    private ApplicationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<InterviewResponse> interviews = new ArrayList<>();

    public JobApplicationResponse() {
    }

    public JobApplicationResponse(Long id, String companyName, String jobTitle, LocalDate applicationDate,
                                  String jobLink, String notes, ApplicationStatus status,
                                  LocalDateTime createdAt, LocalDateTime updatedAt,
                                  List<InterviewResponse> interviews) {
        this.id = id;
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.applicationDate = applicationDate;
        this.jobLink = jobLink;
        this.notes = notes;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.interviews = interviews != null ? interviews : new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<InterviewResponse> getInterviews() {
        return interviews;
    }

    public void setInterviews(List<InterviewResponse> interviews) {
        this.interviews = interviews;
    }
}
