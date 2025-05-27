CREATE TABLE assets
(
    id                 UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    version            INTEGER      NOT NULL,
    name               VARCHAR(255) NOT NULL,
    building_id        UUID,
    deleted            BOOLEAN,
    description        VARCHAR(255),
    created_date       TIMESTAMP,
    created_by         VARCHAR(255),
    last_modified_date TIMESTAMP,
    last_modified_by   VARCHAR(255),
    enterprise_id      UUID,
    tenant_id          UUID,
    FOREIGN KEY (building_id) REFERENCES buildings (id),
    FOREIGN KEY (enterprise_id) REFERENCES enterprises (id),
    FOREIGN KEY (tenant_id) REFERENCES tenant (id)
);
ALTER TABLE assets
    ADD CONSTRAINT chk_tenant_enterprise_nulls
        CHECK ((tenant_id IS NULL AND enterprise_id IS NOT NULL)
            OR (tenant_id IS NOT NULL AND enterprise_id IS NULL));
CREATE INDEX idx_assets_building_id ON assets (building_id);
CREATE INDEX idx_assets_enterprise_id ON assets (enterprise_id);
CREATE INDEX idx_assets_tenant_id ON assets (tenant_id);

alter table activity_data_record
    add column asset_id UUID;
alter table activity_data_record
    add foreign key (asset_id) references assets (id);
CREATE INDEX idx_activity_data_record_asset_id ON activity_data_record (asset_id);

ALTER TABLE activity_data_record
    DROP CONSTRAINT activity_data_record_no_date_overlap;

ALTER TABLE activity_data_record
    ADD CONSTRAINT activity_data_record_no_date_overlap
        EXCLUDE USING gist (
        emission_activity_id WITH =,
        (coalesce(asset_id, '00000000-0000-0000-0000-000000000000'::uuid)) WITH =,
        daterange(start_date, end_date, '[]') WITH &&
        );