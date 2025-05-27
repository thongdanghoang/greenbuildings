ALTER TABLE users
    ADD COLUMN user_role VARCHAR(255) NOT NULL DEFAULT '';

UPDATE users
SET user_role = (SELECT user_role FROM enterprise_users WHERE user_id = users.id);

ALTER TABLE enterprise_users
    DROP COLUMN user_role;

INSERT INTO users (id, version, password, user_role, email,
                   email_verified, phone, phone_verified, first_name, last_name, deleted, locale, theme)
VALUES (gen_random_uuid(),
        0,
        '$2a$10$KgB4aTISvdO7/F6JfHSTyu.w5FBSktdSUTkzRa19EhZrd0urVc62i',
        'SYSTEM_ADMIN',
        'nganntqe170236@greenbuildings.cloud',
        true,
        null,
        false,
        'Ngân',
        'NGUYỄN THỤC',
        false,
        'vi-VN',
        'system');