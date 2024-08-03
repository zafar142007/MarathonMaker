create schema if not exists marathon_maker;
create table if not exists marathon_maker.loop (
    id int auto_increment,
    bib int not null,
    loop int not null,
    lat double default null,
    long double default null,
    checkpoint varchar(50) default null,
    created_at datetime not null default CURRENT_TIMESTAMP,
    updated_at datetime not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY bib_loop (bib, loop)
);

create table if not exists marathon_maker.users (
    id int auto_increment,
    name varchar(100) not null,
    address varchar(500) not null,
    is_awarded tinyint default 0;
    PRIMARY KEY (id)
);


create table if not exists marathon_maker.user_bib (
    bib int not null,
    user_id int not null,
    event_id int not null,
    PRIMARY KEY (bib),
    UNIQUE KEY (event_id, user_id, bib)
);


create table if not exists marathon_maker.event (
    id int auto_increment,
    name varchar(100) not null,
    marathon_id int not null,
    PRIMARY KEY (id)
);

create table if not exists marathon_maker.marathon (
    id int auto_increment,
    name varchar(100) not null,
    PRIMARY KEY (id)
);


