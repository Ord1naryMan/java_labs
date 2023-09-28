create table users(
                      id serial primary key,
                      username varchar(128) not null unique,
                      email varchar(128) not null unique,
                      name varchar(128) not null,
                      password varchar(128) not null,
                      role int not null
);