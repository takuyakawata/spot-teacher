CREATE TABLE IF NOT EXISTS lesson_schedules (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    lesson_reservation_id BIGINT NOT NULL,
    school_id BIGINT NOT NULL,
    teacher_id BIGINT NOT NULL,
    lesson_plan_id BIGINT NOT NULL,
    status ENUM('DOING', 'CANCELED', 'FINISHED') NOT NULL DEFAULT 'DOING',
    title VARCHAR(255) NOT NULL,
    description VARCHAR(2000) NULL,
    location VARCHAR(500) NULL,
    lesson_type ENUM('ONLINE', 'OFFLINE', 'ONLINE_AND_OFFLINE'),
    date DATE NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_lesson_schedules_reservations
    FOREIGN KEY (lesson_reservation_id) REFERENCES lesson_reservations(id),

    INDEX lesson_schedules_lesson_reservation_id (lesson_reservation_id)
);

CREATE TABLE IF NOT EXISTS lesson_schedule_reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    lesson_schedule_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    four_step_rating INT,
    comment TEXT,
    yes_or_no boolean,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_lesson_schedule_reports_lesson_schedule_id
    FOREIGN KEY (lesson_schedule_id) REFERENCES lesson_schedules(id),

    CONSTRAINT chk_four_step_rating CHECK (four_step_rating >= 1 AND four_step_rating <= 4),
    INDEX lesson_schedule_reports_lesson_schedule_id (lesson_schedule_id)
);

CREATE TABLE IF NOT EXISTS lesson_schedule_cancels (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    lesson_schedule_id BIGINT NOT NULL,
    reason TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_lesson_schedule_cancels_lesson_schedule_id
    FOREIGN KEY (lesson_schedule_id) REFERENCES lesson_schedules(id),

    INDEX lesson_schedule_cancels_lesson_schedule_id (lesson_schedule_id)
);

CREATE TABLE IF NOT EXISTS lesson_schedule_educations (
     id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
     lesson_schedule_id BIGINT NOT NULL ,
     education_id BIGINT NOT NULL,
     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
     FOREIGN KEY (education_id) REFERENCES educations (id)
);

CREATE TABLE IF NOT EXISTS lesson_schedule_subjects (
    lesson_schedule_id BIGINT NOT NULL,
    subject_code VARCHAR(50) NOT NULL,
    display_order INT NOT NULL DEFAULT 0,
    PRIMARY KEY (lesson_schedule_id, subject_code),
    FOREIGN KEY (lesson_schedule_id) REFERENCES lesson_schedules(id),
    INDEX idx_lesson_schedule_subject_code (subject_code),
    INDEX idx_lesson_schedule_display_order (display_order)
);

CREATE TABLE IF NOT EXISTS lesson_schedule_grades (
    lesson_schedule_id BIGINT NOT NULL,
    grade_code     varchar(50) NOT NULL,
    display_order INT NOT NULL DEFAULT 0,
    PRIMARY KEY (lesson_schedule_id, grade_code),
    FOREIGN KEY (lesson_schedule_id) REFERENCES lesson_schedules (id),
    INDEX idx_lesson_schedule_grade_code (grade_code),
    INDEX idx_lesson_schedule_display_order (display_order)
);
