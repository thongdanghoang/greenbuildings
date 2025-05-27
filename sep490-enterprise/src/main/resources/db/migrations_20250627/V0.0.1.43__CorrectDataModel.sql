ALTER TABLE activity_type
    RENAME COLUMN tenant_id TO building_id;
ALTER TABLE activity_type
    DROP CONSTRAINT fk_type_tenant;
ALTER TABLE activity_type
    ADD FOREIGN KEY (building_id) REFERENCES buildings(id);


ALTER TABLE emission_activity
    ADD COLUMN building_id UUID;
ALTER TABLE emission_activity
    ADD CONSTRAINT fk_activity_building
        FOREIGN KEY (building_id) REFERENCES buildings (id);

UPDATE emission_activity
SET building_id = bg.building_id
FROM building_group AS bg
WHERE emission_activity.building_group_id = bg.id;

ALTER TABLE emission_activity
    ALTER COLUMN building_id SET NOT NULL;

ALTER TABLE emission_activity
    ALTER COLUMN emission_factor_id SET NOT NULL;

ALTER TABLE activity_data_record ADD CONSTRAINT activity_data_record_no_date_overlap
    EXCLUDE USING gist (emission_activity_id WITH =, daterange(start_date, end_date, '[]') WITH &&);