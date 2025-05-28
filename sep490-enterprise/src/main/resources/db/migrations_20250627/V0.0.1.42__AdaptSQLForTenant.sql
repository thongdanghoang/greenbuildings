ALTER TABLE tenant
    ADD COLUMN email   VARCHAR(255) NOT NULL UNIQUE,
    ADD COLUMN hotline VARCHAR(255);

CREATE TABLE invitations
(
    id                 UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    version            INTEGER      NOT NULL,
    building_group_id  UUID         NOT NULL,
    email              VARCHAR(255) NOT NULL,
    status             VARCHAR(50),
    created_date       TIMESTAMP,
    created_by         VARCHAR(255),
    last_modified_date TIMESTAMP,
    last_modified_by   VARCHAR(255),
    FOREIGN KEY (building_group_id) REFERENCES building_group (id)
);