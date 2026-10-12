package com.jobtracker.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Summary statistics and analytics payload for the frontend dashboard.
 */
public class DashboardStatsResponse {

    private long totalApplications;
    private long interviewsScheduled;
    private long offers;
    private long rejected;
    private Map<String, Long> statusBreakdown = new HashMap<>();
    private List<InterviewResponse> upcomingInterviews;
    private List<JobApplicationResponse> recentApplications;

    public DashboardStatsResponse() {
    }

    public DashboardStatsResponse(long totalApplications, long interviewsScheduled, long offers, long rejected,
                                  Map<String, Long> statusBreakdown,
                                  List<InterviewResponse> upcomingInterviews,
                                  List<JobApplicationResponse> recentApplications) {
        this.totalApplications = totalApplications;
        this.interviewsScheduled = interviewsScheduled;
        this.offers = offers;
        this.rejected = rejected;
        this.statusBreakdown = statusBreakdown;
        this.upcomingInterviews = upcomingInterviews;
        this.recentApplications = recentApplications;
    }

    public long getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(long totalApplications) {
        this.totalApplications = totalApplications;
    }

    public long getInterviewsScheduled() {
        return interviewsScheduled;
    }

    public void setInterviewsScheduled(long interviewsScheduled) {
        this.interviewsScheduled = interviewsScheduled;
    }

    public long getOffers() {
        return offers;
    }

    public void setOffers(long offers) {
        this.offers = offers;
    }

    public long getRejected() {
        return rejected;
    }

    public void setRejected(long rejected) {
        this.rejected = rejected;
    }

    public Map<String, Long> getStatusBreakdown() {
        return statusBreakdown;
    }

    public void setStatusBreakdown(Map<String, Long> statusBreakdown) {
        this.statusBreakdown = statusBreakdown;
    }

    public List<InterviewResponse> getUpcomingInterviews() {
        return upcomingInterviews;
    }

    public void setUpcomingInterviews(List<InterviewResponse> upcomingInterviews) {
        this.upcomingInterviews = upcomingInterviews;
    }

    public List<JobApplicationResponse> getRecentApplications() {
        return recentApplications;
    }

    public void setRecentApplications(List<JobApplicationResponse> recentApplications) {
        this.recentApplications = recentApplications;
    }
}
