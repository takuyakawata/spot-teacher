CREATE TABLE IF NOT EXISTS teachers (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL UNIQUE,
  school_id BIGINT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT fk_teachers_user_id
      FOREIGN KEY (user_id)
          REFERENCES users(id)
          ON DELETE CASCADE,

  CONSTRAINT fk_teachers_school_id
      FOREIGN KEY (school_id)
          REFERENCES schools(id)
          ON DELETE RESTRICT

) COMMENT 'Teacherロールを持つユーザーの情報';
