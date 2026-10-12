package com.jobtracker.enums;

/**
 * Enum representing the current status of a job application.
 * CSE Viva Note: Enums guarantee type safety and prevent invalid status strings
 * from entering the database.
 */
public enum ApplicationStatus {
    APPLIED,
    ASSESSMENT,
    INTERVIEW,
    OFFER,
    REJECTED,
    WITHDRAWN
}
