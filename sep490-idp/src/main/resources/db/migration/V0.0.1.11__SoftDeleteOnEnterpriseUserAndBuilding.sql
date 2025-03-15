ALTER TABLE enterprise_users
    ADD COLUMN deleted BOOLEAN DEFAULT FALSE NOT NULL;
ALTER TABLE enterprise_user_building_permissions
    ADD COLUMN deleted BOOLEAN DEFAULT FALSE NOT NULL;
