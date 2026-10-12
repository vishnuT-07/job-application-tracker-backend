package com.jobtracker.service;

import com.jobtracker.dto.InterviewRequest;
import com.jobtracker.dto.InterviewResponse;
import com.jobtracker.entity.Interview;
import com.jobtracker.entity.JobApplication;
import com.jobtracker.enums.ApplicationStatus;
import com.jobtracker.exception.ResourceNotFoundException;
import com.jobtracker.repository.InterviewRepository;
import com.jobtracker.repository.JobApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service managing interviews for job applications.
 */
@Service
@Transactional
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final JobApplicationRepository applicationRepository;

    public InterviewService(InterviewRepository interviewRepository, JobApplicationRepository applicationRepository) {
        this.interviewRepository = interviewRepository;
        this.applicationRepository = applicationRepository;
    }

    public InterviewResponse scheduleInterview(Long applicationId, InterviewRequest request) {
        Long appId = (applicationId != null) ? applicationId : request.getJobApplicationId();
        if (appId == null) {
            throw new IllegalArgumentException("Job application ID must be provided to schedule an interview");
        }

        JobApplication application = applicationRepository.findById(appId)
                .orElseThrow(() -> new ResourceNotFoundException("JobApplication", "id", appId));

        Interview interview = new Interview();
        interview.setInterviewDate(request.getInterviewDate());
        interview.setRound(request.getRound().trim());
        interview.setType(request.getType().trim());
        interview.setNotes(request.getNotes() != null ? request.getNotes().trim() : null);

        // Add to application (manages bidirectional link)
        application.addInterview(interview);

        // If the application is currently APPLIED or ASSESSMENT, automatically advance status to INTERVIEW!
        if (application.getStatus() == ApplicationStatus.APPLIED || application.getStatus() == ApplicationStatus.ASSESSMENT) {
            application.setStatus(ApplicationStatus.INTERVIEW);
        }

        Interview saved = interviewRepository.save(interview);
        applicationRepository.save(application);

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<InterviewResponse> getInterviewsByApplication(Long applicationId) {
        if (!applicationRepository.existsById(applicationId)) {
            throw new ResourceNotFoundException("JobApplication", "id", applicationId);
        }
        return interviewRepository.findByJobApplicationIdOrderByInterviewDateAsc(applicationId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InterviewResponse> getAllInterviews() {
        return interviewRepository.findAllOrderByInterviewDateDesc().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InterviewResponse> getUpcomingInterviews() {
        return interviewRepository.findByInterviewDateAfterOrderByInterviewDateAsc(LocalDateTime.now()).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InterviewResponse getInterviewById(Long id) {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interview", "id", id));
        return toResponse(interview);
    }

    public InterviewResponse updateInterview(Long id, InterviewRequest request) {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interview", "id", id));

        interview.setInterviewDate(request.getInterviewDate());
        interview.setRound(request.getRound().trim());
        interview.setType(request.getType().trim());
        interview.setNotes(request.getNotes() != null ? request.getNotes().trim() : null);

        Interview updated = interviewRepository.save(interview);
        return toResponse(updated);
    }

    public void deleteInterview(Long id) {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interview", "id", id));

        JobApplication application = interview.getJobApplication();
        if (application != null) {
            application.removeInterview(interview);
        }
        interviewRepository.delete(interview);
    }

    private InterviewResponse toResponse(Interview interview) {
        JobApplication app = interview.getJobApplication();
        return new InterviewResponse(
                interview.getId(),
                app != null ? app.getId() : null,
                app != null ? app.getCompanyName() : null,
                app != null ? app.getJobTitle() : null,
                interview.getInterviewDate(),
                interview.getRound(),
                interview.getType(),
                interview.getNotes(),
                interview.getCreatedAt()
        );
    }
}
