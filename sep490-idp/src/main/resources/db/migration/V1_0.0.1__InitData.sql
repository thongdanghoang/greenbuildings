INSERT INTO users (created_date, created_by, last_modified_date, last_modified_by, id, version, password, email,
                   email_verified, phone, first_name, last_name, deleted, locale)
VALUES (null, null, null, null, 'bb83f2fe-a82c-484a-8d65-891b76da177c', 0,
        '$2a$10$KgB4aTISvdO7/F6JfHSTyu.w5FBSktdSUTkzRa19EhZrd0urVc62i', 'nganntqe170236@greenbuildings.cloud', true, null, 'Ngân',
        'NGUYỄN THỤC', false, 'vi-VN');

INSERT INTO user_authorities (id, user_id, reference_id, permission, deleted)
VALUES ('4fddc253-dbe1-493c-9927-d9b81ed58cd8', 'bb83f2fe-a82c-484a-8d65-891b76da177c', null, 'SYSTEM_ADMIN', false);
