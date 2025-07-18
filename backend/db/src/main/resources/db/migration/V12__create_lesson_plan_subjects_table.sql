CREATE TABLE IF NOT EXISTS lesson_plan_subjects (
    lesson_plan_id BIGINT NOT NULL,
    subject_code VARCHAR(50) NOT NULL,
    display_order INT NOT NULL DEFAULT 0,
    PRIMARY KEY (lesson_plan_id, subject_code),
    FOREIGN KEY (lesson_plan_id) REFERENCES lesson_plans(id),
    INDEX idx_subject_code (subject_code),
    INDEX idx_display_order (display_order)
);
