INSERT INTO credit_convert_ratio (
    created_date, last_modified_date, created_by, last_modified_by,
    id, version, ratio, convert_type
) VALUES
    (NOW(), NOW(), 'sql_by_admin', 'sql_by_admin', gen_random_uuid(), 0, 1, 'MONTH'),
    (NOW(), NOW(), 'sql_by_admin', 'sql_by_admin', gen_random_uuid(), 0, 0.1, 'DEVICE');