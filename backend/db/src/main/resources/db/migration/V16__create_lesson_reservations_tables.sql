CREATE TABLE IF NOT EXISTS lesson_reservations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_id BIGINT NOT NULL,
    school_id BIGINT NOT NULL,
    teacher_id BIGINT NOT NULL,
    lesson_plan_id BIGINT NOT NULL,
    title VARCHAR(500) NOT NULL,
    description VARCHAR(2000) NULL,
    location VARCHAR(500) NULL,
    lesson_type ENUM('ONLINE', 'OFFLINE', 'ONLINE_AND_OFFLINE'),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX lesson_reservation_company_id (company_id),
    INDEX lesson_reservation_title (title),

    CONSTRAINT lesson_reservations_companies_lesson_reservations
    FOREIGN KEY (company_id)
    REFERENCES companies (id)
);

CREATE TABLE IF NOT EXISTS lesson_reservation_dates (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    lesson_reservation_id BIGINT NOT NULL,
    start_date DATE NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT lesson_reservation_dates_lesson_reservations
    FOREIGN KEY (lesson_reservation_id)
    REFERENCES lesson_reservations (id),

    UNIQUE (lesson_reservation_id, start_date)
) COMMENT '授業予約の日付';

CREATE TABLE IF NOT EXISTS lesson_reservation_educations (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    lesson_reservation_id BIGINT NOT NULL ,
    education_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (education_id) REFERENCES educations (id)
);

CREATE TABLE IF NOT EXISTS lesson_reservation_subjects (
    lesson_reservation_id BIGINT NOT NULL,
    subject_code VARCHAR(50) NOT NULL,
    display_order INT NOT NULL DEFAULT 0,
    PRIMARY KEY (lesson_reservation_id, subject_code),
    FOREIGN KEY (lesson_reservation_id) REFERENCES lesson_reservations(id),
    INDEX idx_lesson_reservation_subject_code (subject_code),
    INDEX idx_lesson_reservation_display_order (display_order)
);

CREATE TABLE IF NOT EXISTS lesson_reservation_grades (
    lesson_reservation_id BIGINT NOT NULL,
    grade_code     varchar(50) NOT NULL,
    display_order INT NOT NULL DEFAULT 0,
    PRIMARY KEY (lesson_reservation_id, grade_code),
    FOREIGN KEY (lesson_reservation_id) REFERENCES lesson_reservations (id),
    INDEX idx_lesson_reservation_grade_code (grade_code),
    INDEX idx_lesson_reservation_display_order (display_order)
);