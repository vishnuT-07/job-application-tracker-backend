package com.jobtracker.repository;

import com.jobtracker.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data JPA Repository for Interview.
 */
@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {

    List<Interview> findByJobApplicationIdOrderByInterviewDateAsc(Long jobApplicationId);

    List<Interview> findByInterviewDateAfterOrderByInterviewDateAsc(LocalDateTime dateTime);

    List<Interview> findTop5ByInterviewDateAfterOrderByInterviewDateAsc(LocalDateTime dateTime);

    long countByInterviewDateAfter(LocalDateTime dateTime);

    @Query("SELECT i FROM Interview i ORDER BY i.interviewDate DESC")
    List<Interview> findAllOrderByInterviewDateDesc();
}
