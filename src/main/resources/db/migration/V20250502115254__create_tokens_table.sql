CREATE TABLE barbell_jwt_tokens(
    id BINARY(16) PRIMARY KEY NOT NULL,
    is_blacklisted BOOL default false NOT NULL
)