create table if not exists users (
    id serial primary key,
    name text not null,
    phone text not null,
    email text not null
);