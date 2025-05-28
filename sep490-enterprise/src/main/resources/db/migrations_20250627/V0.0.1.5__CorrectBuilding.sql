ALTER TABLE buildings
    DROP COLUMN floors;
ALTER TABLE buildings
    DROP COLUMN square_meters;

ALTER TABLE buildings
    ADD COLUMN number_of_devices BIGINT NOT NULL default 0;