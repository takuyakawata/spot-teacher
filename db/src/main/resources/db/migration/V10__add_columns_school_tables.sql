ALTER TABLE schools ADD COLUMN school_category enum('ELEMENTARY','JUNIOR_HIGH','HIGH') NOT NULL AFTER name;
ALTER TABLE schools ADD COLUMN post_code varchar(10) NOT NULL UNIQUE AFTER school_category;
ALTER TABLE schools ADD COLUMN phone_number varchar(20) NOT NULL UNIQUE AFTER building_name;
ALTER TABLE schools ADD COLUMN url varchar(500) After phone_number;
