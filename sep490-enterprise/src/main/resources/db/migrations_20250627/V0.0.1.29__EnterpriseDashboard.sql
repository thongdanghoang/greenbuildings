CREATE TABLE dashboard
(
    id            UUID         NOT NULL DEFAULT gen_random_uuid(),
    version       INTEGER      NOT NULL,
    title         VARCHAR(255) NOT NULL UNIQUE,
    src           TEXT         NOT NULL,
    enterprise_id UUID         NOT NULL
);