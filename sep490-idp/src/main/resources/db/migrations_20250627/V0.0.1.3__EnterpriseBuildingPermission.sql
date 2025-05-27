-- Enterprise
CREATE TABLE enterprise
(
    id                 UUID         NOT NULL
);
ALTER TABLE enterprise
    ADD CONSTRAINT enterprise_pk PRIMARY KEY (id);

-- Building
CREATE TABLE building
(
    id                 UUID         NOT NULL,
    enterprise_id      UUID         NOT NULL
);
ALTER TABLE building
    ADD CONSTRAINT building_pk PRIMARY KEY (id),
    ADD CONSTRAINT building_enterprise_fk FOREIGN KEY (enterprise_id) REFERENCES enterprise (id);
CREATE INDEX idx_enterprise_id on building (enterprise_id);

-- Permissions
CREATE TABLE building_permission
(
    building_id         UUID            NOT NULL,
    user_id             UUID            NOT NULL,
    role                VARCHAR(255)    NOT NULL,
    CONSTRAINT pk_building_permission PRIMARY KEY (building_id, user_id)
);
ALTER TABLE building_permission
    ADD CONSTRAINT building_permission_user_fk FOREIGN KEY (user_id) REFERENCES users (id);
CREATE INDEX idx_building_id ON building_permission (building_id);
CREATE INDEX idx_user_id ON building_permission (user_id);
