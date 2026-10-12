package com.jobtracker.service;

import com.jobtracker.dto.InterviewResponse;
import com.jobtracker.dto.JobApplicationRequest;
import com.jobtracker.dto.JobApplicationResponse;
import com.jobtracker.entity.Interview;
import com.jobtracker.entity.JobApplication;
import com.jobtracker.enums.ApplicationStatus;
import com.jobtracker.exception.ResourceNotFoundException;
import com.jobtracker.repository.JobApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service handling business logic for Job Application management.
 * CSE Viva Note: @Service marks this as a Spring-managed service bean.
 * @Transactional ensures database operations within methods are atomic.
 */
@Service
@Transactional
public class JobApplicationService {

    private final JobApplicationRepository applicationRepository;

    public JobApplicationService(JobApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public JobApplicationResponse createApplication(JobApplicationRequest request) {
        JobApplication application = new JobApplication();
        application.setCompanyName(request.getCompanyName().trim());
        application.setJobTitle(request.getJobTitle().trim());
        application.setApplicationDate(request.getApplicationDate());
        application.setJobLink(request.getJobLink() != null ? request.getJobLink().trim() : null);
        application.setNotes(request.getNotes() != null ? request.getNotes().trim() : null);
        application.setStatus(request.getStatus() != null ? request.getStatus() : ApplicationStatus.APPLIED);

        JobApplication saved = applicationRepository.save(application);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<JobApplicationResponse> getAllApplications(String keyword, String company, String jobTitle, ApplicationStatus status) {
        List<JobApplication> applications;

        if (keyword != null && !keyword.trim().isEmpty()) {
            applications = applicationRepository.searchByKeywordAndStatus(keyword.trim(), status);
        } else if ((company != null && !company.trim().isEmpty()) || (jobTitle != null && !jobTitle.trim().isEmpty()) || status != null) {
            applications = applicationRepository.searchAndFilter(
                    company != null ? company.trim() : null,
                    jobTitle != null ? jobTitle.trim() : null,
                    status
            );
        } else {
            applications = applicationRepository.findAll();
            // Sort by applicationDate descending, then ID descending
            applications.sort(Comparator.comparing(JobApplication::getApplicationDate, Comparator.nullsLast(Comparator.reverseOrder()))
                    .thenComparing(JobApplication::getId, Comparator.reverseOrder()));
        }

        return applications.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public JobApplicationResponse getApplicationById(Long id) {
        JobApplication application = findEntityById(id);
        return toResponse(application);
    }

    public JobApplicationResponse updateApplication(Long id, JobApplicationRequest request) {
        JobApplication application = findEntityById(id);

        application.setCompanyName(request.getCompanyName().trim());
        application.setJobTitle(request.getJobTitle().trim());
        application.setApplicationDate(request.getApplicationDate());
        application.setJobLink(request.getJobLink() != null ? request.getJobLink().trim() : null);
        application.setNotes(request.getNotes() != null ? request.getNotes().trim() : null);
        if (request.getStatus() != null) {
            application.setStatus(request.getStatus());
        }

        JobApplication updated = applicationRepository.save(application);
        return toResponse(updated);
    }

    public JobApplicationResponse updateStatus(Long id, ApplicationStatus newStatus) {
        JobApplication application = findEntityById(id);
        application.setStatus(newStatus);
        JobApplication updated = applicationRepository.save(application);
        return toResponse(updated);
    }

    public void deleteApplication(Long id) {
        JobApplication application = findEntityById(id);
        applicationRepository.delete(application);
    }

    public JobApplication findEntityById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobApplication", "id", id));
    }

    public JobApplicationResponse toResponse(JobApplication entity) {
        List<InterviewResponse> interviewResponses = entity.getInterviews() != null
                ? entity.getInterviews().stream()
                .map(i -> toInterviewResponse(i, entity))
                .sorted(Comparator.comparing(InterviewResponse::getInterviewDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList())
                : List.of();

        return new JobApplicationResponse(
                entity.getId(),
                entity.getCompanyName(),
                entity.getJobTitle(),
                entity.getApplicationDate(),
                entity.getJobLink(),
                entity.getNotes(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                interviewResponses
        );
    }

    public InterviewResponse toInterviewResponse(Interview interview, JobApplication parent) {
        return new InterviewResponse(
                interview.getId(),
                parent != null ? parent.getId() : (interview.getJobApplication() != null ? interview.getJobApplication().getId() : null),
                parent != null ? parent.getCompanyName() : (interview.getJobApplication() != null ? interview.getJobApplication().getCompanyName() : null),
                parent != null ? parent.getJobTitle() : (interview.getJobApplication() != null ? interview.getJobApplication().getJobTitle() : null),
                interview.getInterviewDate(),
                interview.getRound(),
                interview.getType(),
                interview.getNotes(),
                interview.getCreatedAt()
        );
    }
}
