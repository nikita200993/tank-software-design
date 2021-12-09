create table if not exists "user"  (
    id bigint generated always as identity,
    login varchar(60) unique not null,
    password varchar(60) not null,
    primary key(id)
);

create table if not exists subscription (
    id bigint generated always as identity,
    publisher_id bigint,
    subscriber_id bigint,
    primary key(id),
    constraint publisher_fk foreign key(publisher_id) references "user"(id),
    constraint subscriber_fk foreign key(subscriber_id) references "user"(id),
    unique(publisher_id, subcriber_id)
);

create table if not exists post (
    id bigint generated always as identity,
    author_id bigint,
    description text,
    image bytea not null,
    created_at timestamp default current_timestamp,
    primary key(id),
    constraint author_fk foreign key(author_id) references "user"(id)
);

create table if not exists comment (
    id bigint generated always as identity,
    author_id bigint,
    post_id bigint,
    content text not null,
    created_at timestamp default current_timestamp,
    primary key(id),
    constraint post_fk foreign key(post_id) references post(id)
);

create table if not exists post_like (
    id bigint generated always as identity,
    author_id bigint,
    post_id bigint,
    primary key(id),
    constraint post_fk foreign key(post_id) references post(id)
);

create table if not exists comment_like (
    id bigint generated always as identity,
    author_id bigint,
    comment_id bigint,
    primary key(id),
    constraint comment_fk foreign key(comment_id) references post(id)
);

-- add tables for user profile data (avatar and other info)