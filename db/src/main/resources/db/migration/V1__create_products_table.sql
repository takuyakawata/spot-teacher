create table if not exists products (
                                        id bigint unsigned not null auto_increment primary key,
                                        name varchar(100) not null,
    price int(10) unsigned not null ,
    description text,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp on update current_timestamp
    );
