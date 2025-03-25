ALTER TABLE emission_factor
    ALTER COLUMN unit_numerator DROP NOT NULL,
    ALTER COLUMN unit_denominator DROP NOT NULL;


INSERT INTO emission_source(id, version, name_vi, name_en, name_zh)
VALUES ('62f3128e-e2dc-4cdf-ac07-b072f9908c30',0, 'Tiêu thụ điện', 'Electricity consumption', '电力消耗');

INSERT INTO emission_factor(version, created_by, created_date, last_modified_date, last_modified_by,
                            co2, ch4, n2o,
                            name_vi, name_en, name_zh,
                            unit_numerator, unit_denominator,
                            emission_source_id,
                            description,
                            valid_from, valid_to,
                            is_direct_emission,
                            energy_conversion_id)
VALUES (0, 'seed_data_sql', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'seed_data_sql',
        0.6925,0,0,
        'Phát thải lưới điện Việt Nam', 'Emissions from the Vietnam Power Grid','越南电网排放',
        'KILOGRAM', 'KWH', '62f3128e-e2dc-4cdf-ac07-b072f9908c30',
        '',
        '2025-01-01 00:00:00','2030-01-01 00:00:00',
        TRUE, null);