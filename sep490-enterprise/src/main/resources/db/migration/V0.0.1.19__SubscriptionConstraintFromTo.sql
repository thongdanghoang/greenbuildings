CREATE EXTENSION btree_gist;
ALTER TABLE subscriptions ADD CONSTRAINT no_date_overlap
    EXCLUDE USING gist (building_id WITH =, daterange(start_date, end_date, '[]') WITH &&);