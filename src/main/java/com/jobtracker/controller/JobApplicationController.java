package com.jobtracker.controller;

import com.jobtracker.dto.InterviewRequest;
import com.jobtracker.dto.InterviewResponse;
import com.jobtracker.dto.JobApplicationRequest;
import com.jobtracker.dto.JobApplicationResponse;
import com.jobtracker.enums.ApplicationStatus;
import com.jobtracker.service.InterviewService;
import com.jobtracker.service.JobApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for managing Job Applications.
 * CSE Viva Note: @RestController combines @Controller and @ResponseBody,
 * automatically serializing Java return objects into JSON.
 */
@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {

    private final JobApplicationService applicationService;
    private final InterviewService interviewService;

    public JobApplicationController(JobApplicationService applicationService, InterviewService interviewService) {
        this.applicationService = applicationService;
        this.interviewService = interviewService;
    }

    /**
     * GET /api/applications
     * Retrieve all applications, optionally filtering by search keyword, company, title, or status.
     */
    @GetMapping
    public ResponseEntity<List<JobApplicationResponse>> getAllApplications(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String jobTitle,
            @RequestParam(required = false) ApplicationStatus status) {
        List<JobApplicationResponse> applications = applicationService.getAllApplications(keyword, company, jobTitle, status);
        return ResponseEntity.ok(applications);
    }

    /**
     * GET /api/applications/{id}
     * Retrieve a single application by ID with its interviews.
     */
    @GetMapping("/{id}")
    public ResponseEntity<JobApplicationResponse> getApplicationById(@PathVariable Long id) {
        JobApplicationResponse response = applicationService.getApplicationById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/applications
     * Create a new job application.
     */
    @PostMapping
    public ResponseEntity<JobApplicationResponse> createApplication(@Valid @RequestBody JobApplicationRequest request) {
        JobApplicationResponse created = applicationService.createApplication(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * PUT /api/applications/{id}
     * Update an existing job application.
     */
    @PutMapping("/{id}")
    public ResponseEntity<JobApplicationResponse> updateApplication(
            @PathVariable Long id,
            @Valid @RequestBody JobApplicationRequest request) {
        JobApplicationResponse updated = applicationService.updateApplication(id, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * PATCH /api/applications/{id}/status
     * Quick status update for Kanban or dropdown changes.
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<JobApplicationResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String statusStr = body.get("status");
        if (statusStr == null || statusStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Status value is required");
        }
        ApplicationStatus newStatus = ApplicationStatus.valueOf(statusStr.trim().toUpperCase());
        JobApplicationResponse updated = applicationService.updateStatus(id, newStatus);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/applications/{id}
     * Delete an application and cascade delete associated interviews.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * POST /api/applications/{id}/interviews
     * Convenience endpoint to schedule an interview under a specific application.
     */
    @PostMapping("/{id}/interviews")
    public ResponseEntity<InterviewResponse> scheduleInterviewForApplication(
            @PathVariable Long id,
            @Valid @RequestBody InterviewRequest request) {
        InterviewResponse scheduled = interviewService.scheduleInterview(id, request);
        return new ResponseEntity<>(scheduled, HttpStatus.CREATED);
    }

    /**
     * GET /api/applications/{id}/interviews
     * Get all interviews belonging to a specific application.
     */
    @GetMapping("/{id}/interviews")
    public ResponseEntity<List<InterviewResponse>> getInterviewsForApplication(@PathVariable Long id) {
        List<InterviewResponse> interviews = interviewService.getInterviewsByApplication(id);
        return ResponseEntity.ok(interviews);
    }
}
