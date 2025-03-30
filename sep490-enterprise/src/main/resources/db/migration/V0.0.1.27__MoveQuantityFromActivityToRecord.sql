ALTER TABLE emission_activity DROP COLUMN quantity;
ALTER TABLE activity_data_record
    ADD COLUMN quantity INT not null default 0;