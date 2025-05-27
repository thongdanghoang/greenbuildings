-- Create table for tenant
CREATE TABLE tenant
(
    id                 UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    name               VARCHAR(255),
    version            INTEGER NOT NULL,
    created_date       TIMESTAMP,
    created_by         VARCHAR(255),
    last_modified_date TIMESTAMP,
    last_modified_by   VARCHAR(255)
);

-- Create table for building_group
CREATE TABLE building_group
(
    id                  UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    version             INTEGER NOT NULL,
    created_date        TIMESTAMP,
    last_modified_date  TIMESTAMP,
    created_by          VARCHAR(255),
    last_modified_by    VARCHAR(255),
    building_group_name VARCHAR(255),
    building_id         UUID    NOT NULL,
    tenant_id           UUID,
    FOREIGN KEY (building_id) REFERENCES buildings (id),
    FOREIGN KEY (tenant_id) REFERENCES tenant (id)
);

-- Create table for group_items
CREATE TABLE group_items
(
    id                 UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    version            INTEGER NOT NULL,
    created_date       TIMESTAMP,
    last_modified_date TIMESTAMP,
    created_by         VARCHAR(255),
    last_modified_by   VARCHAR(255),
    item_name          VARCHAR(255),
    building_group_id  UUID    NOT NULL
);

-- Update table emission_activity_record
ALTER TABLE activity_data_record
    DROP COLUMN emission_activity_id,
    ADD COLUMN group_item_id UUID,
    ADD CONSTRAINT fk_group_item FOREIGN KEY (group_item_id) REFERENCES group_items (id);

-- Update table emission_activity
ALTER TABLE emission_activity
    DROP COLUMN building_id,
    ADD COLUMN building_group_id UUID,
    ADD CONSTRAINT fk_building_group FOREIGN KEY (building_group_id) REFERENCES building_group (id);

ALTER TABLE activity_type
    DROP COLUMN enterprise_id,
    ADD COLUMN tenant_id UUID,
    ADD CONSTRAINT fk_type_tenant FOREIGN KEY (tenant_id) REFERENCES tenant (id);