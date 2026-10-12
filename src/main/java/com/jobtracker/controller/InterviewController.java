package com.jobtracker.controller;

import com.jobtracker.dto.InterviewRequest;
import com.jobtracker.dto.InterviewResponse;
import com.jobtracker.service.InterviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing Interview schedules.
 */
@RestController
@RequestMapping("/api/interviews")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    /**
     * GET /api/interviews
     * Retrieve all interviews or filter by upcoming only.
     */
    @GetMapping
    public ResponseEntity<List<InterviewResponse>> getInterviews(
            @RequestParam(required = false, defaultValue = "false") boolean upcoming) {
        List<InterviewResponse> list = upcoming
                ? interviewService.getUpcomingInterviews()
                : interviewService.getAllInterviews();
        return ResponseEntity.ok(list);
    }

    /**
     * GET /api/interviews/{id}
     * Retrieve a specific interview by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InterviewResponse> getInterviewById(@PathVariable Long id) {
        InterviewResponse response = interviewService.getInterviewById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/interviews
     * Schedule a new interview (expects jobApplicationId inside request).
     */
    @PostMapping
    public ResponseEntity<InterviewResponse> scheduleInterview(@Valid @RequestBody InterviewRequest request) {
        InterviewResponse created = interviewService.scheduleInterview(request.getJobApplicationId(), request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * PUT /api/interviews/{id}
     * Update details of an existing interview.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InterviewResponse> updateInterview(
            @PathVariable Long id,
            @Valid @RequestBody InterviewRequest request) {
        InterviewResponse updated = interviewService.updateInterview(id, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/interviews/{id}
     * Cancel or delete an interview.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInterview(@PathVariable Long id) {
        interviewService.deleteInterview(id);
        return ResponseEntity.noContent().build();
    }
}
