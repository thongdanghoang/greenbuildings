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
    FOREIGN KEY (building_id) REFERENCES buildings (id)
);
CREATE TABLE organizations_assets
(
    asset_id      UUID NOT NULL,
    enterprise_id UUID,
    tenant_id     UUID,
    building_id   UUID,
    PRIMARY KEY (asset_id),
    FOREIGN KEY (asset_id) REFERENCES assets (id),
    FOREIGN KEY (enterprise_id) REFERENCES enterprises (id),
    FOREIGN KEY (tenant_id) REFERENCES tenant (id)
);
ALTER TABLE organizations_assets
    ADD CONSTRAINT chk_tenant_enterprise_nulls
        CHECK (
            (tenant_id IS NULL AND enterprise_id IS NOT NULL)
                OR
            (tenant_id IS NOT NULL AND enterprise_id IS NULL)
            );
CREATE UNIQUE INDEX ux_organizations_assets_enterprise ON organizations_assets (asset_id);
CREATE INDEX organizations_assets_asset_id ON organizations_assets (asset_id);
CREATE INDEX organizations_assets_enterprise_id ON organizations_assets (enterprise_id);
CREATE INDEX organizations_assets_tenant_id ON organizations_assets (tenant_id);