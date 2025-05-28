INSERT INTO enterprises (created_date, created_by, last_modified_date, last_modified_by, id, version, hotline, name, email)
VALUES ('2025-02-12 23:11:26.875648', 'enterprise.owner', '2025-02-12 23:11:26.875648', 'enterprise.owner',
        '664748fa-1312-4456-a88c-1ef187ec9510', 0, '0123456789', 'FPT University', 'fptu.hcm@fpt.edu.vn');


INSERT INTO wallets (created_date, created_by, last_modified_date, last_modified_by, id, version, enterprise_id, balance)
VALUES ('2025-02-12 23:11:26.883390', 'enterprise.owner', '2025-02-12 23:11:26.883390', 'enterprise.owner',
        '14811e94-ede1-4bfa-83d1-5046b49b4fc2', 0, '664748fa-1312-4456-a88c-1ef187ec9510', 0);
