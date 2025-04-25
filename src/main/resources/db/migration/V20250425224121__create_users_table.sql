CREATE TABLE barbell_users
(
    id         BINARY(16)   NOT NULL PRIMARY KEY,
    username   varchar(32)  NOT NULL,
    password   varchar(255) NOT NULL,
    email      varchar(32)  NOT NULL UNIQUE,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
)