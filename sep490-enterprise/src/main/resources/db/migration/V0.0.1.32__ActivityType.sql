CREATE TABLE activity_type
(
    id          UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    version     int,
    name        VARCHAR(255) NOT NULL,
    building_id UUID,
    created_date       TIMESTAMP,
    created_by         VARCHAR(255),
    last_modified_date TIMESTAMP,
    last_modified_by   VARCHAR(255),
    CONSTRAINT fk_activity_type_building FOREIGN KEY (building_id) REFERENCES buildings (id)
);

ALTER TABLE emission_activity
    DROP COLUMN type,
    ADD COLUMN type_id UUID,
    ADD CONSTRAINT fk_emission_activity_type FOREIGN KEY (type_id) REFERENCES activity_type (id);
