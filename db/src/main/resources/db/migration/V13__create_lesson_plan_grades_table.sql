CREATE TABLE IF NOT EXISTS lesson_plan_grades
(
    lesson_plan_id BIGINT      NOT NULL,
    grade_code     varchar(50) NOT NULL,
    display_order INT NOT NULL DEFAULT 0,
    PRIMARY KEY (lesson_plan_id, grade_code),
    FOREIGN KEY (lesson_plan_id) REFERENCES lesson_plans (id) ON DELETE CASCADE,
    INDEX idx_display_order (display_order)
)
