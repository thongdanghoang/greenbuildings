DO
$$
    DECLARE
        building1 UUID := gen_random_uuid();
        building2 UUID := gen_random_uuid();
        building3 UUID := gen_random_uuid();
        source1   UUID; source2 UUID; source3 UUID; source4 UUID; source5 UUID;
        factor1   UUID; factor2 UUID; factor3 UUID; factor4 UUID; factor5 UUID;
    BEGIN
        -- Insert Buildings (assuming buildings table exists)
        INSERT INTO buildings (id, name, address)
        VALUES (building1, 'Office Building A', '123 Green St, Hanoi'),
               (building2, 'Factory B', '456 Industrial Rd, Ho Chi Minh City'),
               (building3, 'Warehouse C', '789 Storage Ln, Da Nang');

        -- Insert Emission Sources
        INSERT INTO emission_source (created_date, created_by, last_modified_date, last_modified_by, building_id, version, name,
                                     type, category, quantity, description)
        VALUES (NOW(), 'admin', NOW(), 'admin', building1, 1, 'HVAC System', 'electricity', 'Scope 2', 5,
                'Heating and cooling system for office'),
               (NOW(), 'admin', NOW(), 'admin', building1, 1, 'Company Cars', 'transport', 'Scope 1', 10,
                'Fleet of gasoline-powered vehicles'),
               (NOW(), 'admin', NOW(), 'admin', building2, 1, 'Boiler', 'fuel', 'Scope 1', 2, 'Natural gas boiler for heating'),
               (NOW(), 'admin', NOW(), 'admin', building2, 1, 'Generator', 'fuel', 'Scope 1', 1,
                'Diesel-powered backup generator'),
               (NOW(), 'admin', NOW(), 'admin', building3, 1, 'Forklifts', 'electricity', 'Scope 2', 8,
                'Electric forklifts for warehouse operations'),
               (NOW(), 'admin', NOW(), 'admin', building1, 1, 'Lighting', 'electricity', 'Scope 2', 200,
                'LED and fluorescent lighting'),
               (NOW(), 'admin', NOW(), 'admin', building2, 1, 'Production Line', 'electricity', 'Scope 2', 15,
                'Machinery for manufacturing'),
               (NOW(), 'admin', NOW(), 'admin', building3, 1, 'Delivery Trucks', 'transport', 'Scope 1', 6,
                'Diesel delivery trucks');

        -- Store Emission Source UUIDs for later use
        SELECT id INTO source1 FROM emission_source WHERE name = 'HVAC System' AND building_id = building1;
        SELECT id INTO source2 FROM emission_source WHERE name = 'Company Cars' AND building_id = building1;
        SELECT id INTO source3 FROM emission_source WHERE name = 'Boiler' AND building_id = building2;
        SELECT id INTO source4 FROM emission_source WHERE name = 'Generator' AND building_id = building2;
        SELECT id INTO source5 FROM emission_source WHERE name = 'Forklifts' AND building_id = building3;

        -- Insert Emission Factors
        INSERT INTO emission_factor (created_date, created_by, last_modified_date, last_modified_by, version, co2, ch4, n2o, name,
                                     factor, unit, source, scope, valid_from, valid_to, is_direct_emission)
        VALUES (NOW(), 'admin', NOW(), 'admin', 1, 0.650000, 0.000025, 0.000010, 'Electricity Grid VN 2025', 0.650000,
                'kg CO2e/kWh', 'Vietnam MoIT', 'Scope 2', '2025-01-01', '2025-12-31', FALSE),
               (NOW(), 'admin', NOW(), 'admin', 1, 2.750000, 0.000100, 0.000050, 'Gasoline VN 2025', 2.750000, 'kg CO2e/liter',
                'IPCC', 'Scope 1', '2025-01-01', '2025-12-31', TRUE),
               (NOW(), 'admin', NOW(), 'admin', 1, 1.850000, 0.000080, 0.000030, 'Natural Gas VN 2025', 1.850000, 'kg CO2e/m3',
                'IPCC', 'Scope 1', '2025-01-01', '2025-12-31', TRUE),
               (NOW(), 'admin', NOW(), 'admin', 1, 2.950000, 0.000120, 0.000060, 'Diesel VN 2025', 2.950000, 'kg CO2e/liter',
                'IPCC', 'Scope 1', '2025-01-01', '2025-12-31', TRUE),
               (NOW(), 'admin', NOW(), 'admin', 1, 0.700000, 0.000030, 0.000015, 'Electricity Grid Regional 2025', 0.700000,
                'kg CO2e/kWh', 'Regional Authority', 'Scope 2', '2025-01-01', '2025-12-31', FALSE);

        -- Store Emission Factor UUIDs for later use
        SELECT id INTO factor1 FROM emission_factor WHERE name = 'Electricity Grid VN 2025';
        SELECT id INTO factor2 FROM emission_factor WHERE name = 'Gasoline VN 2025';
        SELECT id INTO factor3 FROM emission_factor WHERE name = 'Natural Gas VN 2025';
        SELECT id INTO factor4 FROM emission_factor WHERE name = 'Diesel VN 2025';
        SELECT id INTO factor5 FROM emission_factor WHERE name = 'Electricity Grid Regional 2025';

        -- Insert Activity Data (Monthly data for 2025)
        INSERT INTO activity_data (created_date, created_by, last_modified_date, last_modified_by, version, emission_source_id,
                                   emission_factor_id, value, unit, start_date, end_date)
        VALUES
            -- HVAC System (Electricity)
            (NOW(), 'admin', NOW(), 'admin', 1, source1, factor1, 12000.500000, 'kWh', '2025-01-01', '2025-01-31'),
            (NOW(), 'admin', NOW(), 'admin', 1, source1, factor1, 13000.750000, 'kWh', '2025-02-01', '2025-02-28'),
            (NOW(), 'admin', NOW(), 'admin', 1, source1, factor1, 11500.250000, 'kWh', '2025-03-01', '2025-03-31'),
            -- Company Cars (Gasoline)
            (NOW(), 'admin', NOW(), 'admin', 1, source2, factor2, 2500.000000, 'liter', '2025-01-01', '2025-01-31'),
            (NOW(), 'admin', NOW(), 'admin', 1, source2, factor2, 2700.500000, 'liter', '2025-02-01', '2025-02-28'),
            (NOW(), 'admin', NOW(), 'admin', 1, source2, factor2, 2300.750000, 'liter', '2025-03-01', '2025-03-31'),
            -- Boiler (Natural Gas)
            (NOW(), 'admin', NOW(), 'admin', 1, source3, factor3, 5000.250000, 'm3', '2025-01-01', '2025-01-31'),
            (NOW(), 'admin', NOW(), 'admin', 1, source3, factor3, 4800.000000, 'm3', '2025-02-01', '2025-02-28'),
            (NOW(), 'admin', NOW(), 'admin', 1, source3, factor3, 5200.500000, 'm3', '2025-03-01', '2025-03-31'),
            -- Generator (Diesel)
            (NOW(), 'admin', NOW(), 'admin', 1, source4, factor4, 300.750000, 'liter', '2025-01-01', '2025-01-31'),
            (NOW(), 'admin', NOW(), 'admin', 1, source4, factor4, 280.500000, 'liter', '2025-02-01', '2025-02-28'),
            (NOW(), 'admin', NOW(), 'admin', 1, source4, factor4, 320.250000, 'liter', '2025-03-01', '2025-03-31'),
            -- Forklifts (Electricity)
            (NOW(), 'admin', NOW(), 'admin', 1, source5, factor5, 8000.000000, 'kWh', '2025-01-01', '2025-01-31'),
            (NOW(), 'admin', NOW(), 'admin', 1, source5, factor5, 8500.500000, 'kWh', '2025-02-01', '2025-02-28'),
            (NOW(), 'admin', NOW(), 'admin', 1, source5, factor5, 7800.750000, 'kWh', '2025-03-01', '2025-03-31');
    END
$$;