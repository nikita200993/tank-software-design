-- denormalized table for fast read of instagram user feed post is repeated for every follower,
-- column structure + compression will help us with repeated data
create table user_feed
(
    user_id      bigint,
    post_id      bigint,
    author_id    bigint,
    author_login varchar(60),
    description  text,
    image        blob,
    likes_count  bigint,
    created_at   timestamp,
    PRIMARY KEY (user_id, post_id)
);

create table comment
(
    comment_id   bigint,
    post_id      bigint,
    author_id    bigint,
    author_login varchar(60),
    comment_text text,
    PRIMARY KEY (comment_id)
);
