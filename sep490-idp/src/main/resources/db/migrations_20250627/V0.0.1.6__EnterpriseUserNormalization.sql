create table enterprise_users
(
    created_date       TIMESTAMP,
    created_by         VARCHAR(255),
    last_modified_date TIMESTAMP,
    last_modified_by   VARCHAR(255),
    id                 UUID         NOT NULL DEFAULT gen_random_uuid(),
    version            INTEGER      NOT NULL,
    user_id            UUID         NOT NULL,
    enterprise_id      UUID,
    user_role          VARCHAR(255) NOT NULL,
    user_scope         VARCHAR(255) NOT NULL
);
ALTER TABLE enterprise_users
    ADD CONSTRAINT enterprise_users_pk PRIMARY KEY (id);
ALTER TABLE enterprise_users
    ADD CONSTRAINT enterprise_users_fk_user FOREIGN KEY (enterprise_id) REFERENCES users (id);
ALTER TABLE enterprise_users
    ADD CONSTRAINT enterprise_users_unique_user_id UNIQUE (user_id);


create table enterprise_user_building_permissions
(
    id          UUID         NOT NULL DEFAULT gen_random_uuid(),
    user_id     UUID         NOT NULL,
    building_id UUID,
    permission  VARCHAR(255) NOT NULL
);
ALTER TABLE enterprise_user_building_permissions
    ADD CONSTRAINT enterprise_user_building_permissions_pk PRIMARY KEY (id);
ALTER TABLE enterprise_user_building_permissions
    ADD CONSTRAINT enterprise_user_building_permissions_fk_user FOREIGN KEY (user_id) REFERENCES users (id);
CREATE INDEX idx_enterprise_user_building_permissions_user_id ON enterprise_user_building_permissions (user_id);
ALTER TABLE enterprise_user_building_permissions
    ADD CONSTRAINT enterprise_user_building_permissions_building_id_user_id_unique UNIQUE (building_id, user_id);

-- Insert data from users table to enterprise_users table
INSERT INTO enterprise_users (created_date, created_by, last_modified_date, last_modified_by, id, version, user_id,
                              user_role, user_scope)
SELECT u.created_date,
       u.created_by,
       u.last_modified_date,
       u.last_modified_by,
       gen_random_uuid(),
       u.version,
       u.id,
       u.user_role,
       u.user_scope
FROM users u
WHERE u.user_role IS NOT NULL
  AND u.user_scope IS NOT NULL;

-- Remove columns from users table
ALTER TABLE users
    DROP COLUMN user_role,
    DROP COLUMN user_scope;

DROP TABLE building;
DROP TABLE enterprise;
DROP TABLE building_permission;
