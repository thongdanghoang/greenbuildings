ALTER TABLE enterprise_users DROP CONSTRAINT enterprise_users_fk_user;
ALTER TABLE enterprise_users ADD CONSTRAINT enterprise_users_fk_user FOREIGN KEY (user_id) REFERENCES users (id);