package com.jobtracker.repository;

import com.jobtracker.entity.JobApplication;
import com.jobtracker.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for JobApplication.
 * CSE Viva Note: Spring Data JPA generates standard CRUD queries (save, findById, findAll, delete)
 * at runtime without needing manual SQL or boilerplate implementation code.
 */
@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByStatus(ApplicationStatus status);

    long countByStatus(ApplicationStatus status);

    List<JobApplication> findTop5ByOrderByCreatedAtDesc();

    /**
     * Flexible search and filter query supporting company name, job title, and application status.
     * Passing NULL or empty string matches all records for that criterion.
     */
    @Query("SELECT j FROM JobApplication j WHERE " +
           "(:company IS NULL OR :company = '' OR LOWER(j.companyName) LIKE LOWER(CONCAT('%', :company, '%'))) AND " +
           "(:jobTitle IS NULL OR :jobTitle = '' OR LOWER(j.jobTitle) LIKE LOWER(CONCAT('%', :jobTitle, '%'))) AND " +
           "(:status IS NULL OR j.status = :status) " +
           "ORDER BY j.applicationDate DESC, j.id DESC")
    List<JobApplication> searchAndFilter(
            @Param("company") String company,
            @Param("jobTitle") String jobTitle,
            @Param("status") ApplicationStatus status
    );

    /**
     * Keyword search across both company name and job title, plus optional status filter.
     */
    @Query("SELECT j FROM JobApplication j WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           " LOWER(j.companyName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           " LOWER(j.jobTitle) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:status IS NULL OR j.status = :status) " +
           "ORDER BY j.applicationDate DESC, j.id DESC")
    List<JobApplication> searchByKeywordAndStatus(
            @Param("keyword") String keyword,
            @Param("status") ApplicationStatus status
    );
}
