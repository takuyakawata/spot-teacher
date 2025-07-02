CREATE TABLE IF NOT EXISTS lesson_plans (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_id BIGINT NOT NULL,
    title VARCHAR(500),
    description VARCHAR(2000) NULL,
    location VARCHAR(500) NULL,
    lesson_type ENUM('ONLINE', 'OFFLINE', 'ONLINE_AND_OFFLINE'),
    annual_max_executions BIGINT COMMENT '年間の最大実施回数',
    published BOOLEAN DEFAULT FALSE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX lessonplan_company_id (company_id),
    INDEX lessonplan_title (title),

    CONSTRAINT lesson_plans_companies_lesson_plans
        FOREIGN KEY (company_id)
            REFERENCES companies (id)
            ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS lesson_plan_dates (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    lesson_plan_id BIGINT NOT NULL,
    start_month BIGINT NOT NULL COMMENT '開講期間（開始月）',
    start_day BIGINT NOT NULL COMMENT '開講期間（開始日）',
    end_month BIGINT NOT NULL COMMENT '開講期間（終了月）',
    end_day BIGINT NOT NULL COMMENT '開講期間（終了日）',
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT lesson_plan_dates_lesson_plans
        FOREIGN KEY (lesson_plan_id)
            REFERENCES lesson_plans (id)
            ON DELETE CASCADE

) COMMENT '授業計画の日付';
