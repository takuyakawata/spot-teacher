create table if not exists companies (
     id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
     name varchar(50) NOT NULL,
     prefecture bigint NOT NULL,
     city varchar(255) NOT NULL,
     street varchar(255) NULL,
     post_code varchar(7) NOT NULL,
     phone_number varchar(255) NOT NULL,
     url varchar(1000) NULL,
     created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
     updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
     INDEX `company_name` (`name`)
);
