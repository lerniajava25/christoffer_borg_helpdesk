alter table users
    add column role varchar(50) default 'USER' not null;