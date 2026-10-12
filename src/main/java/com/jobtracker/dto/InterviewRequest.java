package com.jobtracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Request payload for scheduling or updating an interview.
 */
public class InterviewRequest {

    private Long jobApplicationId;

    @NotNull(message = "Interview date and time is required")
    private LocalDateTime interviewDate;

    @NotBlank(message = "Interview round name is required (e.g. Technical Round 1)")
    private String round;

    @NotBlank(message = "Interview type is required (e.g. Online, In-person)")
    private String type;

    private String notes;

    public InterviewRequest() {
    }

    public InterviewRequest(Long jobApplicationId, LocalDateTime interviewDate, String round, String type, String notes) {
        this.jobApplicationId = jobApplicationId;
        this.interviewDate = interviewDate;
        this.round = round;
        this.type = type;
        this.notes = notes;
    }

    public Long getJobApplicationId() {
        return jobApplicationId;
    }

    public void setJobApplicationId(Long jobApplicationId) {
        this.jobApplicationId = jobApplicationId;
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
}
