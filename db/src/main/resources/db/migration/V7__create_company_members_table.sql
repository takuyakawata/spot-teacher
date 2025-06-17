CREATE TABLE IF NOT EXISTS company_members (
     id BIGINT AUTO_INCREMENT PRIMARY KEY,
     user_id BIGINT NOT NULL,
     company_id BIGINT NOT NULL,
     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

     UNIQUE KEY uk_user_company (user_id, company_id),

     CONSTRAINT fk_company_members_user_id
         FOREIGN KEY (user_id)
             REFERENCES users(id)
             ON DELETE CASCADE
) COMMENT 'CompanyMemberロールを持つユーザーの追加情報';
