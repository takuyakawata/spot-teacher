CREATE TABLE IF NOT EXISTS educations (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL UNIQUE,
  display_order INT NOT NULL DEFAULT 0,
  is_active BOOLEAN NOT NULL DEFAULT FALSE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_name (name),
  INDEX idx_display_order (display_order)
);

CREATE TABLE IF NOT EXISTS lesson_plans_educations (
   lesson_plan_id BIGINT NOT NULL,
   education_id BIGINT NOT NULL,
   display_order INT NOT NULL DEFAULT 0,
   PRIMARY KEY (lesson_plan_id, education_id),
   FOREIGN KEY (lesson_plan_id) REFERENCES lesson_plans(id),
   FOREIGN KEY (education_id) REFERENCES educations(id),
   INDEX idx_education_id (education_id),
   INDEX idx_display_order (display_order)
);
