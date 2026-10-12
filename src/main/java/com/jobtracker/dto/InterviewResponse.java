package com.jobtracker.dto;

import java.time.LocalDateTime;

/**
 * DTO representing an Interview returned to the client.
 */
public class InterviewResponse {

    private Long id;
    private Long jobApplicationId;
    private String companyName;
    private String jobTitle;
    private LocalDateTime interviewDate;
    private String round;
    private String type;
    private String notes;
    private LocalDateTime createdAt;

    public InterviewResponse() {
    }

    public InterviewResponse(Long id, Long jobApplicationId, String companyName, String jobTitle,
                             LocalDateTime interviewDate, String round, String type, String notes,
                             LocalDateTime createdAt) {
        this.id = id;
        this.jobApplicationId = jobApplicationId;
        this.companyName = companyName;
        this.jobTitle = jobTitle;
        this.interviewDate = interviewDate;
        this.round = round;
        this.type = type;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJobApplicationId() {
        return jobApplicationId;
    }

    public void setJobApplicationId(Long jobApplicationId) {
        this.jobApplicationId = jobApplicationId;
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

    public LocalDateTime getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(LocalDateTime interviewDate) {
        this.interviewDate = interviewDate;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
