ALTER TABLE power_bi_authorities
    ADD COLUMN scope VARCHAR(255) DEFAULT 'ENTERPRISE';
ALTER TABLE power_bi_authorities
    ADD CONSTRAINT check_scope CHECK (scope IN ('BUILDING', 'ENTERPRISE'));

CREATE TABLE power_bi_building_permissions
(
    power_bi_authority_id UUID NOT NULL,
    building_id           UUID NOT NULL,
    CONSTRAINT fk_power_bi_authorities_id FOREIGN KEY (power_bi_authority_id) REFERENCES power_bi_authorities (id) ON DELETE
        CASCADE
);

CREATE INDEX idx_power_bi_building_permissions_power_bi_authority_id ON power_bi_building_permissions (power_bi_authority_id);