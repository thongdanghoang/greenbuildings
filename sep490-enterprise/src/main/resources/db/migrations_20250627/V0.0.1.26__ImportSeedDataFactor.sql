ALTER TABLE emission_factor
ADD COLUMN active BOOLEAN DEFAULT FALSE;


DO
$$
    DECLARE
        factor_data   TEXT[][] := ARRAY [
            ['Hệ số điều chỉnh mê-tan (MCF) của bãi chôn lấp không quản lý-sâu ( độ sâu từ 5m trở lên)', 'Methane Correction Factor (MCF) of Deep Unmanaged Landfills (5m or more in depth)','深层无人管理垃圾填埋场（深度 5 米或以上）的甲烷校正系数 (MCF)',
                '0', '0.8', '0', null, null, 'Phát thải từ bãi chôn lấp chất thải rắn', null, 'true'],
            ['Hệ số điều chỉnh mê-tan (MCF) của bãi chôn lấp không quản lý-nông ( độ sâu dưới 5m )','Methane Correction Factor (MCF) of Unmanaged Landfills-Shallow (depth less than 5m)','无人管理的浅层垃圾填埋场（深度小于 5 米）的甲烷校正系数 (MCF)',
                '0', '0.4', '0', null, null, 'Phát thải từ bãi chôn lấp chất thải rắn', null, 'true'],
            ['Hệ số điều chỉnh mê-tan (MCF) của bãi chôn lấp quản lý- kỵ khí','Methane Correction Factor (MCF) of Managed-Anaerobic Landfills','管理型厌氧垃圾填埋场的甲烷校正系数 (MCF)',
                '0', '1', '0', null, null, 'Phát thải từ bãi chôn lấp chất thải rắn', null, 'true'],
            ['Hệ số điều chỉnh mê-tan (MCF) của bãi chôn lấp quản lý tốt - bán hiếu khí','Methane Correction Factor (MCF) of Well-Managed Landfills - Semi-Aerobic','管理良好的垃圾填埋场的甲烷校正系数 (MCF) - 半需氧',
                '0', '0.5', '0', null, null, 'Phát thải từ bãi chôn lấp chất thải rắn', null, 'true'],
            ['Hệ số điều chỉnh mê-tan (MCF) của bãi chôn lấp quản lý kém - bán hiếu khí','Methane Correction Factor (MCF) of Poorly Managed - Semi-Aerobic Landfills','管理不善的半需氧垃圾填埋场的甲烷校正系数 (MCF)',
                '0', '0.7', '0', null, null, 'Phát thải từ bãi chôn lấp chất thải rắn', null, 'true'],
            ['Hệ số điều chỉnh mê-tan (MCF) của bãi chôn lấp quản lý tốt - Sục khí liên tục','Methane Correction Factor (MCF) of a Well-Managed Landfill - Continuous Aeration','管理良好的垃圾填埋场的甲烷校正系数 (MCF) - 连续曝气',
                '0', '0.4', '0', null, null, 'Phát thải từ bãi chôn lấp chất thải rắn', null, 'true'],
            ['Hệ số điều chỉnh mê-tan (MCF) của bãi chôn lấp quản lý kém - Sục khí liên tục','Methane Correction Factor (MCF) for Poorly Managed Landfills - Continuous Aeration','管理不善的垃圾填埋场的甲烷校正系数 (MCF) - 连续曝气',
                '0', '0.7', '0', null, null, 'Phát thải từ bãi chôn lấp chất thải rắn', null, 'true'],
            ['Hệ số điều chỉnh mê-tan (MCF) của bãi chôn lấp còn lại','Methane Correction Factor (MCF) of the Residual Landfill','剩余垃圾填埋场的甲烷校正系数 (MCF)',
                '0', '0.6', '0', null, null, 'Phát thải từ bãi chôn lấp chất thải rắn', null, 'true'],

            ['Carbon hữu cơ phân huỷ (DOC) của thức ăn. chất hữu cơ','Degradable organic carbon (DOC) of food. organic matter','食物的可降解有机碳（DOC）。有机质',
                '0', '15', '0', 'PERCENT', 'KILOGRAM', 'Phát thải từ bãi chôn lấp chất thải rắn', null, 'true'],
            ['Carbon hữu cơ phân huỷ (DOC) của cây cối','Decomposable organic carbon (DOC) of plants','植物的可分解有机碳（DOC）',
                '0', '20', '0', 'PERCENT', 'KILOGRAM', 'Phát thải từ bãi chôn lấp chất thải rắn', null, 'true'],
            ['Carbon hữu cơ phân huỷ (DOC) của giấy','Degradable organic carbon (DOC) of paper','纸张的可降解有机碳（DOC）',
                '0', '40', '0', 'PERCENT', 'KILOGRAM', 'Phát thải từ bãi chôn lấp chất thải rắn', null, 'true'],
            ['Carbon hữu cơ phân huỷ (DOC) của gỗ','Degradable organic carbon (DOC) of wood','木材的可降解有机碳（DOC）',
                '0', '43', '0', 'PERCENT', 'KILOGRAM', 'Phát thải từ bãi chôn lấp chất thải rắn', null, 'true'],
            ['Carbon hữu cơ phân huỷ (DOC) của dệt may','Degradable organic carbon (DOC) of textiles','纺织品的可降解有机碳 (DOC)',
                '0', '24', '0', 'PERCENT', 'KILOGRAM', 'Phát thải từ bãi chôn lấp chất thải rắn', null, 'true'],
            ['Carbon hữu cơ phân huỷ (DOC) của tã lót','Degradable organic carbon (DOC) of diapers','尿布的可生物降解有机碳 (DOC)',
                '0', '24', '0', 'PERCENT', 'KILOGRAM', 'Phát thải từ bãi chôn lấp chất thải rắn', null, 'true'],
            ['Carbon hữu cơ phân huỷ (DOC) của bùn thải','Degradable organic carbon (DOC) of sludge','污泥的可降解有机碳（DOC）',
                '0', '5', '0', 'PERCENT', 'KILOGRAM', 'Phát thải từ bãi chôn lấp chất thải rắn', null, 'true'],
            ['Carbon hữu cơ phân huỷ (DOC) của chát thải công nghiệp','Degradable organic carbon (DOC) of industrial waste','工业废弃物的可降解有机碳（DOC）',
                '0', '15', '0', 'PERCENT', 'KILOGRAM', 'Phát thải từ bãi chôn lấp chất thải rắn', null, 'true'],
            ['Tỷ lệ DOC dễ phân huỷ trong điều kiện kỵ khí (DOCf) - Chất thải khó phân huỷ: gỗ. sản phẩm chế tạo từ gỗ. cành cây.…','Anaerobic biodegradable DOC fraction (DOCf) - Hardly biodegradable waste: wood. wood products. tree branches.…','厌氧可生物降解DOC部分（DOCf）- 难以生物降解的废物：木材。木制品树枝。…',
                '0', '10', '0', 'PERCENT', 'KILOGRAM', 'Phát thải từ bãi chôn lấp chất thải rắn', null, 'true'],
            ['Tỷ lệ DOC dễ phân huỷ trong điều kiện kỵ khí (DOCf) - Chất thải phân huỷ trung bình : giấy. các sản phẩm dệt may. tã lót.','Anaerobic biodegradable DOC fraction (DOCf) - Medium biodegradable waste: paper. textile products. diapers.','厌氧可生物降解DOC组分（DOCf）-中等可生物降解废物：纸张。纺织产品尿布。',
                '0', '50', '0', 'PERCENT', 'KILOGRAM', 'Phát thải từ bãi chôn lấp chất thải rắn', null, 'true'],
            ['Tỷ lệ DOC dễ phân huỷ trong điều kiện kỵ khí (DOCf) - Chất thải dễ phân huỷ  : thức ăn thừa. cỏ ( rác vườn trừ cây cảnh)','Anaerobic biodegradable DOC ratio (DOCf) - Biodegradable waste: food waste, grass (garden waste except ornamental plants)','厌氧可生物降解DOC部分（DOCf）-可生物降解的废物：食物垃圾。草（除观赏植物外的园林废弃物）',
                '0', '70', '0', 'PERCENT', 'KILOGRAM', 'Phát thải từ bãi chôn lấp chất thải rắn', null, 'true'],
            ['Tỷ lệ lượng CH₄ trong khí từ bãi rác','CH₄ content in landfill gas','垃圾填埋气中CH₄含量',
                '0', '50', '0', 'PERCENT', 'KILOGRAM', 'Phát thải từ bãi chôn lấp chất thải rắn', null, 'true'],

            ['Xử lý chất thải bằng phương pháp ủ phân sinh học (composting) - Hệ số phát thải theo trọng lượng khô','Composting Waste Treatment - Emission Factor on a Dry Weight Basis','堆肥废物处理 - 干重排放因子',
                '0.6', '10', '0', 'GRAM', 'KILOGRAM', 'Xử lý chất thải rắn bằng phương pháp sinh học', null, 'true'],
            ['Xử lý chất thải bằng phương pháp ủ phân sinh học (composting) - Hệ số phát thải theo trọng lượng ướt','Composting Waste Treatment - Emission Factor on Wet Weight Basis','堆肥废物处理 - 湿重排放因子',
                '0.24', '4', '0', 'GRAM', 'KILOGRAM', 'Xử lý chất thải rắn bằng phương pháp sinh học', null, 'true'],
            ['Xử lý bằng hầm Biogas kỵ khí- Hệ số phát thải theo trọng lượng khô','Anaerobic Biogas Treatment - Emission Factor by Dry Weight','厌氧沼气处理 - 干重排放因子',
                '0', '2', '0', 'GRAM', 'KILOGRAM', 'Xử lý chất thải rắn bằng phương pháp sinh học', null, 'true'],
            ['Xử lý bằng hầm Biogas kỵ khí- Hệ số phát thải theo trọng lượng ướt','Anaerobic Biogas Treatment - Emission Factor by Wet Weight','厌氧沼气处理 - 湿重排放因子',
                '0', '0.8', '0', 'GRAM', 'KILOGRAM', 'Xử lý chất thải rắn bằng phương pháp sinh học', null, 'true'],

            ['Hàm lượng khô theo thành phần chất thải rắn của nhựa','Dry content according to solid waste composition of plastic','根据固体废物中塑料的成分计算干含量',
                '100', '0', '0', 'PERCENT', null, 'Thiêu đốt chất thải (chất thải rắn sinh hoạt)', null, 'true'],
            ['Hàm lượng khô theo thành phần chất thải rắn của dệt may','Dry content according to the composition of textile solid waste','根据纺织固体废弃物的成分计算的干含量',
                '80', '0', '0', 'PERCENT', null, 'Thiêu đốt chất thải (chất thải rắn sinh hoạt)', null, 'true'],
            ['Hàm lượng khô theo thành phần chất thải rắn của gỗ','Dry content according to solid waste composition of wood','根据木材固体废物成分计算的干物质含量',
                '85', '0', '0', 'PERCENT', null, 'Thiêu đốt chất thải (chất thải rắn sinh hoạt)', null, 'true'],
            ['Hàm lượng khô theo thành phần chất thải rắn của tã lót','Dry content according to solid waste composition of diapers','根据尿布固体废物成分计算的干物质含量',
                '40', '0', '0', 'PERCENT', null, 'Thiêu đốt chất thải (chất thải rắn sinh hoạt)', null, 'true'],
            ['Hàm lượng khô theo thành phần chất thải rắn của thực phẩm. chất hữu cơ','Dry content according to solid waste composition of food. organic matter','根据食物固体废物的成分计算的干含量。有机质',
                '40', '0', '0', 'PERCENT', null, 'Thiêu đốt chất thải (chất thải rắn sinh hoạt)', null, 'true'],
            ['Hàm lượng khô theo thành phần chất thải rắn của cây cối','Dry content according to solid waste composition of plants','根据工厂固体废物成分计算的干物质含量',
                '40', '0', '0', 'PERCENT', null, 'Thiêu đốt chất thải (chất thải rắn sinh hoạt)', null, 'true'],
            ['Hàm lượng khô theo thành phần chất thải rắn của giấy','Dry content according to solid waste composition of paper','根据纸张固体废物成分计算的干物质含量',
                '90', '0', '0', 'PERCENT', null, 'Thiêu đốt chất thải (chất thải rắn sinh hoạt)', null, 'true'],
            ['Tỷ lệ hàm lượng carbon của nhựa ( CF)','Carbon content ratio of plastic ( CF)','塑料含碳量比（CF）',
                '75', '0', '0', 'PERCENT', null, 'Thiêu đốt chất thải (chất thải rắn sinh hoạt)', null, 'true'],
            ['Tỷ lệ hàm lượng carbon của dệt may ( CF)','Carbon content of textiles (CF)','纺织品的碳含量（CF）',
                '50', '0', '0', 'PERCENT', null, 'Thiêu đốt chất thải (chất thải rắn sinh hoạt)', null, 'true'],
            ['Tỷ lệ hàm lượng carbon của gỗ ( CF)','Carbon content of wood (CF)','木材碳含量（CF）',
                '50', '0', '0', 'PERCENT', null, 'Thiêu đốt chất thải (chất thải rắn sinh hoạt)', null, 'true'],
            ['Tỷ lệ hàm lượng carbon của tã lót ( CF)','Carbon content ratio of diapers ( CF)','纸尿裤含碳量比（CF）',
                '70', '0', '0', 'PERCENT', null, 'Thiêu đốt chất thải (chất thải rắn sinh hoạt)', null, 'true'],
            ['Tỷ lệ hàm lượng carbon của thực phẩm. chất hữu cơ ( CF)','The carbon content of food. organic matter ( CF)','食物中碳含量的百分比。有机物（CF）',
                '38', '0', '0', 'PERCENT', null, 'Thiêu đốt chất thải (chất thải rắn sinh hoạt)', null, 'true'],
            ['Tỷ lệ hàm lượng carbon của cây cối ( CF)','Carbon content of trees ( CF)','树木的碳含量（CF）',
                '49', '0', '0', 'PERCENT', null, 'Thiêu đốt chất thải (chất thải rắn sinh hoạt)', null, 'true'],
            ['Tỷ lệ hàm lượng carbon của giấy ( CF)','Carbon content of paper ( CF)','纸张含碳量（CF）',
                '46', '0', '0', 'PERCENT', null, 'Thiêu đốt chất thải (chất thải rắn sinh hoạt)', null, 'true'],
            ['Tỷ lệ carbon hoá thạch trong tổng số carbon của nhựa ( FCF)','Fossil carbon fraction of total carbon of plastic (FCF)','化石碳占塑料碳总量 (FCF) 的比例',
                '100', '0', '0', 'PERCENT', null, 'Thiêu đốt chất thải (chất thải rắn sinh hoạt)', null, 'true'],
            ['Tỷ lệ carbon hoá thạch trong tổng số carbon của dệt may ( FCF)','Fossil carbon share of total textile carbon (FCF)','化石碳占纺织品碳排放总量的份额（FCF）',
                '20', '0', '0', 'PERCENT', null, 'Thiêu đốt chất thải (chất thải rắn sinh hoạt)', null, 'true'],
            ['Tỷ lệ carbon hoá thạch trong tổng số carbon của tã lót ( FCF)','Fossil carbon fraction of total carbon of diapers (FCF)','尿布总碳含量的化石碳分数（FCF）',
                '10', '0', '0', 'PERCENT', null, 'Thiêu đốt chất thải (chất thải rắn sinh hoạt)', null, 'true'],
            ['Tỷ lệ carbon hoá thạch trong tổng số carbon của cây cối ( FCF)','Fossil carbon fraction of total tree carbon (FCF)','化石碳占树木总碳含量 (FCF)',
                '0', '0', '0', 'PERCENT', null, 'Thiêu đốt chất thải (chất thải rắn sinh hoạt)', null, 'true'],
            ['Tỷ lệ carbon hoá thạch trong tổng số carbon của giấy ( FCF)','Fossil carbon fraction of total paper carbon (FCF)','化石碳占纸张碳总量的比例（FCF）',
                '1', '0', '0', 'PERCENT', null, 'Thiêu đốt chất thải (chất thải rắn sinh hoạt)', null, 'true'],
            ['Hệ số oxy hoá OF','OF oxidation coefficient','OF氧化系数',
                '100', '0', '0', 'PERCENT', null, 'Thiêu đốt chất thải', null, 'true'],

            ['Hệ số phát thải của Lò đốt vỉ động liên tục', 'Emission factor of Continuous Moving Grate Incinerator', '连续移动炉排焚烧炉的排放因子',
                '0', '0.0002', '47', 'KILOGRAM', 'TERAGRAM', 'Thiêu đốt chất thải', null, 'true'],
            ['Hệ số phát thải của Lò đốt tầng sôi liên tục', 'Emission factor of Continuous Fluidized Bed Incinerator', '连续流化床焚烧炉的排放因子',
                '0', '0', '67', 'KILOGRAM', 'TERAGRAM', 'Thiêu đốt chất thải', null, 'true'],
            ['Hệ số phát thải của Lò đốt vỉ động bán liên tục', 'Emission factor of Semi-Continuous Moving Grate Incinerator', '半连续移动炉排焚烧炉的排放因子',
                '0', '0.006', '41', 'KILOGRAM', 'TERAGRAM', 'Thiêu đốt chất thải', null, 'true'],
            ['Hệ số phát thải của Lò đốt tầng sôi bán liên tục', 'Emission factor of Semi-Continuous Fluidized Bed Incinerator', '半连续流化床焚烧炉的排放因子',
                '0', '0.188', '68', 'KILOGRAM', 'TERAGRAM', 'Thiêu đốt chất thải', null, 'true'],
            ['Hệ số phát thải của Lò đốt vỉ động - đốt hàng loạt', 'Emission factor of Batch Moving Grate Incinerator', '间歇式移动炉排焚烧炉的排放因子',
                '0', '0.06', '56', 'KILOGRAM', 'TERAGRAM', 'Thiêu đốt chất thải', null, 'true'],
            ['Hệ số phát thải của Lò đốt tầng sôi - đốt hàng loạt', 'Emission factor of Batch Fluidized Bed Incinerator', '间歇式流化床焚烧炉的排放因子',
                '0', '0.237', '221', 'KILOGRAM', 'TERAGRAM', 'Thiêu đốt chất thải', null, 'true'],

            ['Hệ số oxy hoá OF','OF oxidation coefficient','OF氧化系数',
                '71', '0', '0', 'PERCENT', null, 'Đốt lộ thiên chất thải', null, 'true'],
            ['Hệ số phát thải của Đốt lộ thiên chất thải', 'Emission factor of Open Burning of Waste', '露天焚烧废弃物的排放因子',
                '0', '6.5', '150', 'GRAM', 'MEGAGRAM', 'Đốt lộ thiên chất thải', null, 'true'],

            ['Khả năng phát sinh khí CH₄ tối đa - B0','Maximum CH₄ gas generation potential - B0','最大 CH₄ 气体生成潜力 - B0',
                '0', '0.6', '0', null, null, 'Xử lý và xả nước thải sinh hoạt', null, 'true'],
            ['Hệ số hiệu chỉnh metan của hệ thống xử lý nước thải tập trung. hiếu khí ','Methane correction factor of centralized wastewater treatment system. aerobic','集中式污水处理系统甲烷校正系数。有氧',
                '0', '0.3', '0', null, null, 'Xử lý và xả nước thải sinh hoạt', null, 'true'],
            ['Hệ số hiệu chỉnh metan của hệ thống tự hoại','Methane correction factor for septic systems','化粪池系统的甲烷校正系数',
                '0', '0.5', '0', null, null, 'Xử lý và xả nước thải sinh hoạt', null, 'true'],
            ['Hệ số hiệu chỉnh metan của nước thải sinh hoạt không được xử lý. xả ra sông. hồ. biển','Methane correction factor of untreated domestic wastewater discharged into rivers, lakes, seas','未处理的生活污水的甲烷校正因子。排入河流湖。海',
                '0', '0.1', '0', null, null, 'Xử lý và xả nước thải sinh hoạt', null, 'true'],
            ['Hệ số phát thải','Emission factor','排放因子',
                '0', '0', '0.01', null, null, 'Xử lý và xả nước thải sinh hoạt', null, 'true'],
            ['Khả năng phát sinh khí CH₄ tối đa - B0','Maximum CH₄ gas generation potential - B0','最大 CH₄ 气体生成潜力 - B0',
                '0', '0.25', '0', null, null, 'Xử lý và xả nước thải công nghiệp', null, 'true'],
            ['Hệ số hiệu chỉnh metan của hệ thống xử lý nước thải tập trung. hiếu khí','Methane correction factor of centralized wastewater treatment system. aerobic','集中式污水处理系统甲烷校正系数。有氧',
                '0', '0.3', '0', null, null, 'Xử lý và xả nước thải công nghiệp', null, 'true'],
            ['Hệ số hiệu chỉnh metan của hệ thống xử lý bán hiếu khí ( kị khí nông)','Methane correction factor of semi-aerobic (aerobic) treatment system','准好氧（好氧）处理系统甲烷修正系数',
                '0', '0.2', '0', null, null, 'Xử lý và xả nước thải công nghiệp', null, 'true'],
            ['Hệ số hiệu chỉnh metan của hệ thống kỵ khí sâu','Methane correction factor of deep anaerobic system','深度厌氧系统甲烷校正因子',
                '0', '0.8', '0', null, null, 'Xử lý và xả nước thải công nghiệp', null, 'true'],
            ['Hệ số hiệu chỉnh metan của nước thải sinh hoạt không được xử lý. xả ra sông. hồ. biển','Methane correction factor of untreated domestic wastewater discharged into rivers, lakes, seas','未处理的生活污水的甲烷校正因子。排入河流湖。海',
                '0', '0.1', '0', null, null, 'Xử lý và xả nước thải công nghiệp', null, 'true']];
        source_id     UUID;
        conversion_id UUID;
        i             INT;
    BEGIN
        FOR i IN 1 .. array_length(factor_data, 1)
            LOOP
                SELECT id
                INTO source_id
                FROM emission_source
                WHERE name_vi = factor_data[i][9];

                IF source_id IS NULL THEN
                    RAISE EXCEPTION 'No matching emission_source found for name_vi: %', factor_data[i][1];
                END IF;

                IF factor_data[i][11] = 'false' THEN
                    SELECT ec.id
                    INTO conversion_id
                    FROM energy_conversion ec
                             JOIN fuel f ON ec.fuel_id = f.id
                    WHERE f.name_vi = factor_data[i][10];

                    IF conversion_id IS NULL THEN
                        RAISE EXCEPTION 'No matching energy_conversion found for name_vi: %', factor_data[i][10];
                    END IF;
                ELSE
                    conversion_id := NULL; -- Direct emission, no conversion
                END IF;

                -- Step 4: Insert into emission_factor
                INSERT INTO emission_factor (version, created_by, created_date, last_modified_date, last_modified_by,
                                             co2, ch4, n2o,
                                             name_vi, name_en, name_zh,
                                             unit_numerator, unit_denominator,
                                             emission_source_id,
                                             description,
                                             valid_from, valid_to,
                                             is_direct_emission,
                                             energy_conversion_id,
                                             active)
                VALUES (0, 'seed_data_sql', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'seed_data_sql',
                        factor_data[i][4]::DECIMAL(20, 6), factor_data[i][5]::DECIMAL(20, 6), factor_data[i][6]::DECIMAL(20, 6),
                        factor_data[i][1], factor_data[i][2], factor_data[i][3],
                        factor_data[i][7], factor_data[i][8],
                        source_id,
                        '',
                        '2025-01-01 00:00:00',
                        '2030-01-01 00:00:00',
                        factor_data[i][11]::BOOLEAN,
                        conversion_id,
                        false);
            END LOOP;
    END
$$;
