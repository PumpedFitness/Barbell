CREATE TABLE tokens(
    token_id BINARY(16) PRIMARY KEY NOT NULL,
    is_blacklisted BOOL default false NOT NULL
)