ALTER TABLE activity_type
    ADD COLUMN enterprise_id UUID;

-- Add foreign key constraint to enterprise
ALTER TABLE activity_type
    ADD CONSTRAINT fk_activity_type_enterprise
        FOREIGN KEY (enterprise_id) REFERENCES enterprises (id);

-- Drop old building relationship
ALTER TABLE activity_type
    DROP CONSTRAINT fk_activity_type_building,
    DROP COLUMN building_id;