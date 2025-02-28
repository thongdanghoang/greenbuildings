INSERT INTO public.enterprises (created_date, created_by, last_modified_date, last_modified_by, id, version, hotline, name, email)
VALUES ('2025-02-16 20:36:14.312269', 'Anonymous', '2025-02-16 20:36:14.312269', 'Anonymous',
        'f87565e6-e229-4e9b-b74d-4e085d05395e', 0, '0301930055', 'FPT University', 'fptu.hcm@fpt.edu.vn');
INSERT INTO public.buildings (created_date, created_by, last_modified_date, last_modified_by, id, version, enterprise_id, name,
                              number_of_devices, latitude, longitude)
VALUES ('2025-02-16 20:45:00.595269', 'fptu.hcm@fpt.edu.vn', '2025-02-16 20:45:00.595269', 'fptu.hcm@fpt.edu.vn',
        '99c0932f-e8b1-44a3-a93a-514f992ef9cf', 0, 'f87565e6-e229-4e9b-b74d-4e085d05395e', 'FPT Quy Nhon', 500,
        13.796696244798206, 109.21856202185155);
INSERT INTO public.buildings (created_date, created_by, last_modified_date, last_modified_by, id, version, enterprise_id, name,
                              number_of_devices, latitude, longitude)
VALUES ('2025-02-16 21:58:43.298707', 'fptu.hcm@fpt.edu.vn', '2025-02-16 21:58:43.298707', 'fptu.hcm@fpt.edu.vn',
        'c746d31a-f132-4ceb-9c96-f14c5e629531', 0, 'f87565e6-e229-4e9b-b74d-4e085d05395e', 'FPT Can Tho', 123, 10.013038185016901,
        105.7318063080311);
INSERT INTO public.buildings (created_date, created_by, last_modified_date, last_modified_by, id, version, enterprise_id, name,
                              number_of_devices, latitude, longitude)
VALUES ('2025-02-16 22:31:08.315223', 'fptu.hcm@fpt.edu.vn', '2025-02-16 22:31:08.315223', 'fptu.hcm@fpt.edu.vn',
        '648af581-2635-4eed-9aee-b592085c16f3', 0, 'f87565e6-e229-4e9b-b74d-4e085d05395e', 'FPT Hà Nội', 123, 21.013579709497332,
        105.52510991692544);
INSERT INTO public.buildings (created_date, created_by, last_modified_date, last_modified_by, id, version, enterprise_id, name,
                              number_of_devices, latitude, longitude)
VALUES ('2025-02-16 20:36:27.019184', 'fptu.hcm@fpt.edu.vn', '2025-02-16 20:36:27.019184', 'fptu.hcm@fpt.edu.vn',
        'c8636202-c2ff-49e7-a43a-267a4dfa1658', 0, 'f87565e6-e229-4e9b-b74d-4e085d05395e', 'FPT Ho Chi Minh', 123,
        10.84139877290165, 106.81006103754045);
INSERT INTO public.credit_packages (created_date, created_by, last_modified_date, last_modified_by, id, version, number_of_credits, price)
VALUES ('2025-02-16 20:45:00.595269', 'fptu.hcm@fpt.edu.vn', '2025-02-16 20:45:00.595269', 'fptu.hcm@fpt.edu.vn',
        'b9c3a8f7-5d4e-4e2a-80c7-1f9d2b45e1a3', 0, 100,10000000);
        
INSERT INTO credit_convert_ratio (
    created_date, last_modified_date, created_by, last_modified_by,
    id, version, ratio, convert_type
) VALUES
    (NOW(), NOW(), 'admin', 'admin', gen_random_uuid(), 0, 1, 'MONTH'),
    (NOW(), NOW(), 'admin', 'admin', gen_random_uuid(), 0, 0.1, 'DEVICE');