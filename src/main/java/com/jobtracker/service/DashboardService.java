package com.jobtracker.service;

import com.jobtracker.dto.DashboardStatsResponse;
import com.jobtracker.dto.InterviewResponse;
import com.jobtracker.dto.JobApplicationResponse;
import com.jobtracker.enums.ApplicationStatus;
import com.jobtracker.repository.InterviewRepository;
import com.jobtracker.repository.JobApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service aggregating statistics and progress analytics for the dashboard.
 */
@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final JobApplicationRepository applicationRepository;
    private final InterviewRepository interviewRepository;
    private final JobApplicationService applicationService;

    public DashboardService(JobApplicationRepository applicationRepository,
                            InterviewRepository interviewRepository,
                            JobApplicationService applicationService) {
        this.applicationRepository = applicationRepository;
        this.interviewRepository = interviewRepository;
        this.applicationService = applicationService;
    }

    public DashboardStatsResponse getDashboardStats() {
        long totalApplications = applicationRepository.count();
        long interviewsScheduled = interviewRepository.countByInterviewDateAfter(LocalDateTime.now());
        long offers = applicationRepository.countByStatus(ApplicationStatus.OFFER);
        long rejected = applicationRepository.countByStatus(ApplicationStatus.REJECTED);

        // Breakdown of each status
        Map<String, Long> statusBreakdown = new LinkedHashMap<>();
        for (ApplicationStatus status : ApplicationStatus.values()) {
            statusBreakdown.put(status.name(), applicationRepository.countByStatus(status));
        }

        // Top 5 upcoming interviews
        List<InterviewResponse> upcomingInterviews = interviewRepository
                .findTop5ByInterviewDateAfterOrderByInterviewDateAsc(LocalDateTime.now()).stream()
                .map(i -> applicationService.toInterviewResponse(i, i.getJobApplication()))
                .collect(Collectors.toList());

        // Recent 5 applications
        List<JobApplicationResponse> recentApplications = applicationRepository
                .findTop5ByOrderByCreatedAtDesc().stream()
                .map(applicationService::toResponse)
                .collect(Collectors.toList());

        return new DashboardStatsResponse(
                totalApplications,
                interviewsScheduled,
                offers,
                rejected,
                statusBreakdown,
                upcomingInterviews,
                recentApplications
        );
    }
}
