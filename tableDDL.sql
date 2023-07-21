create table member
(
    idx      int unsigned auto_increment
        primary key,
    user_id  varchar(20) null,
    password varchar(100) null,
    email    varchar(100) null,
    name     varchar(20) null,
    gender   varchar(3) null,
    birthday date null,
    addr     varchar(50) null,
    tel      varchar(15) null,
    reg_date datetime null,
    mod_date datetime null
) charset = utf8;

create table member_role
(
    idx        int auto_increment
        primary key,
    member_idx int unsigned not null,
    role       varchar(10) null
) charset = utf8;

create table refresh_token
(
    idx        int auto_increment
        primary key,
    member_idx int unsigned not null,
    token      varchar(200) not null
) charset = utf8;