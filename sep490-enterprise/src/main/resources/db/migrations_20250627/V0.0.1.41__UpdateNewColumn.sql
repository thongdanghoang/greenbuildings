ALTER TABLE group_items
    ADD COLUMN item_description TEXT;

ALTER TABLE activity_data_record
    ADD COLUMN emission_activity_id uuid NOT NULL,
    ADD CONSTRAINT fk_activity_data_record_emission_activityFOREIGN
        FOREIGN KEY (emission_activity_id) REFERENCES emission_activity (id);