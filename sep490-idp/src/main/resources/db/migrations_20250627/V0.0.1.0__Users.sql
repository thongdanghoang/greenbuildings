CREATE TABLE users
(
    created_date       TIMESTAMP,
    created_by         VARCHAR(255),
    last_modified_date TIMESTAMP,
    last_modified_by   VARCHAR(255),
    id                 UUID        NOT NULL DEFAULT gen_random_uuid(),
    version            INTEGER     NOT NULL,
    password           VARCHAR(72),
    email              VARCHAR(255),
    email_verified     BOOLEAN     NOT NULL DEFAULT FALSE,
    phone              VARCHAR(16),
    phone_verified     BOOLEAN     NOT NULL DEFAULT FALSE,
    first_name         VARCHAR(50),
    last_name          VARCHAR(100)
);
ALTER TABLE users
    ADD CONSTRAINT users_pk PRIMARY KEY (id),
    ADD CONSTRAINT users_email_unique UNIQUE (email);
