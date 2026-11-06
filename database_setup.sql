-- =============================================================================
-- ConfSync — Smart Conference Management System
-- Database Setup Script for MySQL (localhost:3306)
-- PBLJ University Java Project
-- =============================================================================

CREATE DATABASE IF NOT EXISTS `confsync_db` 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE `confsync_db`;

-- 1. Users Table
CREATE TABLE IF NOT EXISTS `users` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `email` VARCHAR(150) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `role` VARCHAR(20) NOT NULL,
    `created_at` DATETIME NOT NULL,
    `updated_at` DATETIME NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Conferences Table
CREATE TABLE IF NOT EXISTS `conferences` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `title` VARCHAR(255) NOT NULL,
    `description` TEXT,
    `category` VARCHAR(100) NOT NULL,
    `start_date` DATE NOT NULL,
    `end_date` DATE NOT NULL,
    `registration_deadline` DATE NOT NULL,
    `venue` VARCHAR(255) NOT NULL,
    `max_participants` INT NOT NULL,
    `status` VARCHAR(20) NOT NULL,
    `created_at` DATETIME NOT NULL,
    `updated_at` DATETIME NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. Speakers Table
CREATE TABLE IF NOT EXISTS `speakers` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `organization` VARCHAR(255) NOT NULL,
    `designation` VARCHAR(255) NOT NULL,
    `biography` TEXT,
    `expertise` VARCHAR(255) NOT NULL,
    `phone` VARCHAR(50),
    `created_at` DATETIME NOT NULL,
    `updated_at` DATETIME NULL,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. Sessions Table
CREATE TABLE IF NOT EXISTS `sessions` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `conference_id` BIGINT NOT NULL,
    `speaker_id` BIGINT NULL,
    `title` VARCHAR(255) NOT NULL,
    `description` TEXT,
    `session_date` DATE NOT NULL,
    `start_time` TIME NOT NULL,
    `end_time` TIME NOT NULL,
    `hall` VARCHAR(100) NOT NULL,
    `category` VARCHAR(100),
    `max_capacity` INT,
    `created_at` DATETIME NOT NULL,
    `updated_at` DATETIME NULL,
    FOREIGN KEY (`conference_id`) REFERENCES `conferences`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`speaker_id`) REFERENCES `speakers`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. Registrations Table
CREATE TABLE IF NOT EXISTS `registrations` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `participant_id` BIGINT NOT NULL,
    `conference_id` BIGINT NOT NULL,
    `registration_date` DATETIME NOT NULL,
    `status` VARCHAR(20) NOT NULL,
    `attendance_status` BOOLEAN NOT NULL DEFAULT FALSE,
    `qr_code` TEXT,
    `created_at` DATETIME NOT NULL,
    `updated_at` DATETIME NULL,
    UNIQUE KEY `unique_participant_conference` (`participant_id`, `conference_id`),
    FOREIGN KEY (`participant_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`conference_id`) REFERENCES `conferences`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. Attendance Table
CREATE TABLE IF NOT EXISTS `attendance` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `registration_id` BIGINT NOT NULL UNIQUE,
    `check_in_time` DATETIME NOT NULL,
    `created_at` DATETIME NOT NULL,
    `updated_at` DATETIME NULL,
    FOREIGN KEY (`registration_id`) REFERENCES `registrations`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7. Papers Table
CREATE TABLE IF NOT EXISTS `papers` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `author_id` BIGINT NOT NULL,
    `conference_id` BIGINT NOT NULL,
    `title` VARCHAR(255) NOT NULL,
    `abstract` TEXT NOT NULL,
    `category` VARCHAR(100) NOT NULL,
    `keywords` VARCHAR(255) NOT NULL,
    `file_path` VARCHAR(500),
    `status` VARCHAR(30) NOT NULL,
    `submitted_at` DATETIME NOT NULL,
    `created_at` DATETIME NOT NULL,
    `updated_at` DATETIME NULL,
    FOREIGN KEY (`author_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`conference_id`) REFERENCES `conferences`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 8. Reviewer Assignments Table
CREATE TABLE IF NOT EXISTS `reviewer_assignments` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `paper_id` BIGINT NOT NULL,
    `reviewer_id` BIGINT NOT NULL,
    `assigned_at` DATETIME NOT NULL,
    `status` VARCHAR(20) NOT NULL,
    `created_at` DATETIME NOT NULL,
    `updated_at` DATETIME NULL,
    UNIQUE KEY `unique_paper_reviewer` (`paper_id`, `reviewer_id`),
    FOREIGN KEY (`paper_id`) REFERENCES `papers`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`reviewer_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 9. Reviews Table
CREATE TABLE IF NOT EXISTS `reviews` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `paper_id` BIGINT NOT NULL,
    `reviewer_id` BIGINT NOT NULL,
    `originality_score` DOUBLE NOT NULL,
    `technical_quality_score` DOUBLE NOT NULL,
    `relevance_score` DOUBLE NOT NULL,
    `presentation_score` DOUBLE NOT NULL,
    `overall_score` DOUBLE NOT NULL,
    `comments` TEXT,
    `recommendation` VARCHAR(30) NOT NULL,
    `reviewed_at` DATETIME NOT NULL,
    `created_at` DATETIME NOT NULL,
    `updated_at` DATETIME NULL,
    UNIQUE KEY `unique_paper_reviewer_review` (`paper_id`, `reviewer_id`),
    FOREIGN KEY (`paper_id`) REFERENCES `papers`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`reviewer_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 10. Certificates Table
CREATE TABLE IF NOT EXISTS `certificates` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `participant_id` BIGINT NOT NULL,
    `conference_id` BIGINT NOT NULL,
    `certificate_number` VARCHAR(100) NOT NULL UNIQUE,
    `verification_code` VARCHAR(50) NOT NULL UNIQUE,
    `generated_at` DATETIME NOT NULL,
    `file_path` VARCHAR(500),
    `created_at` DATETIME NOT NULL,
    `updated_at` DATETIME NULL,
    UNIQUE KEY `unique_cert_participant_conf` (`participant_id`, `conference_id`),
    FOREIGN KEY (`participant_id`) REFERENCES `users`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`conference_id`) REFERENCES `conferences`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 11. Notifications Table
CREATE TABLE IF NOT EXISTS `notifications` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `title` VARCHAR(255) NOT NULL,
    `message` TEXT NOT NULL,
    `is_read` BOOLEAN NOT NULL DEFAULT FALSE,
    `created_at` DATETIME NOT NULL,
    `updated_at` DATETIME NULL,
    FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

