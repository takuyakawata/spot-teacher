CREATE TABLE IF NOT EXISTS user_credentials(
    user_id BIGINT PRIMARY KEY,
    password_hash VARCHAR(255) NOT NULL,
    last_password_change_at DATETIME NULL,
    failed_login_attempts INT NOT NULL DEFAULT 0,
    locked_until DATETIME NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_credentials_user_id FOREIGN KEY (user_id) REFERENCES users(id)
);

ALTER TABLE users DROP COLUMN password_hash;
