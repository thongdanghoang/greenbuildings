ALTER TABLE users
    ADD COLUMN enterprise_id UUID,
    ADD COLUMN user_scope    VARCHAR(255);

UPDATE users
SET enterprise_id = eu.enterprise_id,
    user_scope    = eu.user_scope
FROM enterprise_users eu
WHERE eu.user_id = users.id;

DROP TABLE IF EXISTS enterprise_users;