-- ==========================================================
-- Job Application Tracking System - PostgreSQL Setup Script
-- CSE Viva Reference: This script creates the PostgreSQL database.
-- ==========================================================

-- 1. Create the database (run in pgAdmin, psql, or Render PostgreSQL Console)
CREATE DATABASE job_tracker_db;

-- 2. Connect to the database
\c job_tracker_db;

-- Note: Spring Boot with 'spring.jpa.hibernate.ddl-auto=update'
-- will automatically create all tables ('job_applications' and
-- 'interviews') and sequences/identity constraints upon application startup.
