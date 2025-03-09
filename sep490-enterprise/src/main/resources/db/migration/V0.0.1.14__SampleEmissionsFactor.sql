-- IMPORT INTO i18n, emission_source
DO $$
    DECLARE
        translations TEXT[][] := ARRAY [
            ['Công nghiệp năng lượng', 'Energy Industry', '能源工业'],
            ['Công nghiệp sản xuất và xây dựng', 'Manufacturing and Construction Industry', '制造与建筑工业'],
            ['Giao thông vận tải hàng không nội địa', 'Domestic Air Transport', '国内航空运输'],
            ['Giao thông vận tải đường bộ', 'Road Transport', '公路运输'],
            ['Giao thông vận tải đường sắt', 'Rail Transport', '铁路运输'],
            ['Giao thông vận tải đường thuỷ nội địa và hàng hải nội địa', 'Inland Waterway and Domestic Maritime Transport', '内河及国内海上运输'],
            ['Thương mại và dịch vụ', 'Commerce and Services', '商业与服务'],
            ['Dân dụng', 'Residential', '民用'],
            ['Nông nghiệp, lâm nghiệp và thuỷ sản', 'Agriculture, Forestry, and Fisheries', '农业、林业和渔业'],
            ['Khai thác than hầm lò', 'Underground Coal Mining', '地下煤炭开采'],
            ['Khai thác dầu', 'Oil Extraction', '石油开采'],
            ['Khai thác khí tự nhiên', 'Natural Gas Extraction', '天然气开采'],
            ['Phát thải từ bãi chôn lấp chất thải rắn', 'Emissions from Solid Waste Landfills', '固体废物填埋场排放'],
            ['Xử lý chất thải rắn bằng phương pháp sinh học', 'Biological Solid Waste Treatment', '生物固体废物处理'],
            ['Thiêu đốt chất thải (chất thải rắn sinh hoạt)', 'Incineration of Waste (Household Solid Waste)', '焚烧废物（生活固体废物）'],
            ['Thiêu đốt chất thải', 'Waste Incineration', '废物焚烧'],
            ['Thiêu đốt chất thải - Lò đốt vỉ động liên tục', 'Waste Incineration - Continuous Grate Incinerator', '废物焚烧 - 连续炉排焚烧炉'],
            ['Thiêu đốt chất thải - Lò đốt tầng sôi liên tục', 'Waste Incineration - Continuous Fluidized Bed Incinerator', '废物焚烧 - 连续流化床焚烧炉'],
            ['Thiêu đốt chất thải - Lò đốt vỉ động bán liên tục', 'Waste Incineration - Semi-Continuous Grate Incinerator', '废物焚烧 - 半连续炉排焚烧炉'],
            ['Thiêu đốt chất thải - Lò đốt tầng sôi bán liên tục', 'Waste Incineration - Semi-Continuous Fluidized Bed Incinerator', '废物焚烧 - 半连续流化床焚烧炉'],
            ['Thiêu đốt chất thải - Lò đốt vỉ động - đốt hàng loạt', 'Waste Incineration - Batch Grate Incinerator', '废物焚烧 - 批次炉排焚烧炉'],
            ['Thiêu đốt chất thải - Lò đốt tầng sôi - đốt hàng loạt', 'Waste Incineration - Batch Fluidized Bed Incinerator', '废物焚烧 - 批次流化床焚烧炉'],
            ['Đốt lộ thiên chất thải', 'Open Burning of Waste', '露天焚烧废物'],
            ['Xử lý và xả nước thải sinh hoạt', 'Domestic Wastewater Treatment and Discharge', '生活污水处理与排放'],
            ['Xử lý và xả nước thải công nghiệp', 'Industrial Wastewater Treatment and Discharge', '工业污水处理与排放'],
            ['Sử dụng điện', 'Electricity Use', '电力使用'],
            ['Khai thác than lộ thiên', 'Open-pit coal mining', '露天煤矿开采'],
            ['Nông nghiệp. lâm nghiệp và thuỷ sản', 'Agriculture, forestry, and fisheries', '农业、林业和渔业']
            ];
    BEGIN
        FOR i IN 1..array_length(translations, 1)
            LOOP
                INSERT INTO emission_source (version, name_vi, name_en, name_zh)
                VALUES (0, translations[i][1], translations[i][2], translations[i][3]);
            END LOOP;
    END
$$;


-- IMPORT INTO i18n, fuel, energy conversion
DO $$
    DECLARE
        fuel_names TEXT[][] := ARRAY[
            -- Format: [Vietnamese, English, Chinese, conversion_value, conversion_unit_numerator, conversion_unit_denominator]
            ['Dầu thô', 'Crude Oil', '原油', '44.5', 'TERAJOULE', 'GIGAGRAM'],
            ['Orimulsion', 'Orimulsion', '奥里乳化油', '27.5', 'TERAJOULE', 'GIGAGRAM'],
            ['Khí tự nhiên lỏng', 'Natural Gas Liquids', '天然气液体', NULL, NULL, NULL],
            ['Khí tự nhiên', 'Natural Gas', '天然气', ' 0.037683', 'TERAJOULE', 'GIGAGRAM'],
            ['Etan', 'Ethane', '乙烷', '47.5', 'TERAJOULE', 'GIGAGRAM'],
            ['Propan', 'Propane', '丙烷', '46.3', 'TERAJOULE', 'GIGAGRAM'],
            ['Butan', 'Butane', '丁烷', '45.5', 'TERAJOULE', 'GIGAGRAM'],
            ['Xăng động cơ', 'Motor Gasoline', '车用汽油', '44.3', 'TERAJOULE', 'THOUSAND_CUBIC_METER'],
            ['Xăng hàng không', 'Aviation Gasoline', '航空汽油', '44.3', 'TERAJOULE', 'THOUSAND_CUBIC_METER'],
            ['Xăng phản lực', 'Jet Gasoline', '喷气汽油', '44.1', 'TERAJOULE', 'THOUSAND_CUBIC_METER'],
            ['Nhiên liệu phản lực', 'Jet Kerosene', '喷气煤油', '44.1', 'TERAJOULE', 'THOUSAND_CUBIC_METER'],
            ['Dầu hỏa', 'Kerosene', '煤油', '34.4', 'TERAJOULE', 'THOUSAND_CUBIC_METER'],
            ['Dầu hỏa khác', 'Other Kerosene', '其他煤油', '43.8', 'TERAJOULE', 'THOUSAND_CUBIC_METER'],
            ['Dầu đá phiến', 'Shale Oil', '页岩油', '40.2', 'TERAJOULE', 'GIGAGRAM'],
            ['Dầu diesel', 'Gas Diesel Oil', '燃气柴油', '43', 'TERAJOULE', 'THOUSAND_CUBIC_METER'],
            ['Dầu nhiên liệu', 'Fuel Oil', '燃料油', '39.358', 'TERAJOULE', 'THOUSAND_CUBIC_METER'],
            ['Dầu nhiên liệu cặn', 'Residual Fuel Oil', '残余燃料油', '40.4', 'TERAJOULE', 'GIGAGRAM'],
            ['Khí dầu mỏ hóa lỏng', 'Liquefied Petroleum Gas', '液化石油气', '47.3', 'TERAJOULE', 'GIGAGRAM'],
            ['Naphta', 'Naphtha', '石脑油', '44.5', 'TERAJOULE', 'THOUSAND_CUBIC_METER'],
            ['Khí tự nhiên', 'Natural Gas', '天然气', '39.9', 'TERAJOULE', 'MILLION_CUBIC_METER'],
            ['Dầu bôi trơn', 'Lubricants', '润滑油', '40.2', 'TERAJOULE', 'GIGAGRAM'],
            ['Than cốc dầu mỏ', 'Petroleum Coke', '石油焦', '32.5', 'TERAJOULE', 'GIGAGRAM'],
            ['Nguyên liệu lọc dầu', 'Refinery Feedstocks', '炼油原料', '43.3', 'TERAJOULE', 'GIGAGRAM'],
            ['Khí nhà máy lọc dầu', 'Refinery Gas', '炼油厂气体', '44.5', 'TERAJOULE', 'GIGAGRAM'],
            ['Sáp paraffin', 'Paraffin Waxes', '石蜡', '40', 'TERAJOULE', 'GIGAGRAM'],
            ['Sản phẩm dầu mỏ khác', 'Other Petroleum Products', '其他石油产品', '41', 'TERAJOULE', 'GIGAGRAM'],
            ['Than antraxit', 'Anthracite', '无烟煤', '30', 'TERAJOULE', 'GIGAGRAM'],
            ['Than cốc', 'Coking Coal', '焦煤', '28.2', 'TERAJOULE', 'GIGAGRAM'],
            ['Than bitum khác', 'Other Bituminous Coal', '其他烟煤', '25.8', 'TERAJOULE', 'GIGAGRAM'],
            ['Than bán bitum', 'Sub-bituminous Coal', '次烟煤', '18.9', 'TERAJOULE', 'GIGAGRAM'],
            ['Than non', 'Lignite', '褐煤', '11.9', 'TERAJOULE', 'GIGAGRAM'],
            ['Dầu đá phiến và cát dầu', 'Oil Shale and Tar Sands', '油页岩和油砂', '8.9', 'TERAJOULE', 'GIGAGRAM'],
            ['Than non ép viên', 'Brown Coal Briquettes', '褐煤球', '20', 'TERAJOULE', 'GIGAGRAM'],
            ['Nhiên liệu ép', 'Patent Fuel', '专利燃料', '0.020', 'TERAJOULE', 'MEGAGRAM'],
            ['Than cốc lò luyện / Than cốc từ than non', 'Coke Oven Coke / Lignite Coke', '焦炉焦炭 / 褐煤焦炭', '0.0282', 'TERAJOULE', 'MEGAGRAM'],
            ['Than cốc', 'Gas Coke', '煤气焦炭', '0.0285', 'TERAJOULE', 'MEGAGRAM'],
            ['Hắc ín than', 'Coal Tar', '煤焦油', '0.0326', 'TERAJOULE', 'MEGAGRAM'],
            ['Khí nhà máy khí', 'Gas Works Gas', '煤气厂气体', '0.0176', 'TERAJOULE', 'THOUSAND_CUBIC_METER'],
            ['Khí lò cốc', 'Coke Oven Gas', '焦炉煤气', '0.0176', 'TERAJOULE', 'THOUSAND_CUBIC_METER'],
            ['Khí lò cao', 'Blast Furnace Gas', '高炉煤气', '0.0036', 'TERAJOULE', 'THOUSAND_CUBIC_METER'],
            ['Khí lò luyện thép oxy', 'Oxygen Steel Furnace Gas', '氧气转炉煤气', '0.0070', 'TERAJOULE', 'THOUSAND_CUBIC_METER'],
            ['Khí tự nhiên (khô)', 'Natural Gas (Dry)', '天然气（干）', '0.0480', 'TERAJOULE', 'MEGAGRAM'],
            ['Rác thải đô thị - phần không sinh khối', 'Municipal Wastes (Non-Biomass Fraction)', '城市废物（非生物质部分）', '0.0100', 'TERAJOULE', 'MEGAGRAM'],
            ['Rác thải công nghiệp', 'Industrial Wastes', '工业废物', '0.0150', 'TERAJOULE', 'MEGAGRAM'],
            ['Dầu thải', 'Waste Oils', '废油', '0.0402', 'TERAJOULE', 'MEGAGRAM'],
            ['Than bùn', 'Peat', '泥炭', '0.0098', 'TERAJOULE', 'MEGAGRAM'],
            ['Gỗ / Phế thải gỗ', 'Wood / Wood Waste', '木材 / 木材废料', '0.0156', 'TERAJOULE', 'MEGAGRAM'],
            ['Dung dịch sunphit', 'Sulphite Lyes', '亚硫酸盐液', '0.0118', 'TERAJOULE', 'MEGAGRAM'],
            ['Sinh khối rắn sơ cấp khác', 'Other Primary Solid Biomass', '其他初级固体生物质', '0.0125', 'TERAJOULE', 'MEGAGRAM'],
            ['Than củi', 'Charcoal', '木炭', '0.0295', 'TERAJOULE', 'MEGAGRAM'],
            ['Xăng sinh học', 'Biogasoline', '生物汽油', '0.0270', 'TERAJOULE', 'MEGAGRAM'],
            ['Diesel sinh học', 'Biodiesels', '生物柴油', '0.0270', 'TERAJOULE', 'MEGAGRAM'],
            ['Nhiên liệu sinh học lỏng khác', 'Other Liquid Biofuels', '其他液体生物燃料', '0.0270', 'TERAJOULE', 'MEGAGRAM'],
            ['Khí bãi rác', 'Landfill Gas', '垃圾填埋气', '0.0190', 'TERAJOULE', 'THOUSAND_CUBIC_METER'],
            ['Khí bùn', 'Sludge Gas', '污泥气', '0.0190', 'TERAJOULE', 'THOUSAND_CUBIC_METER'],
            ['Khí sinh học khác', 'Other Biogas', '其他沼气', '0.0190', 'TERAJOULE', 'THOUSAND_CUBIC_METER'],
            ['Sinh khối', 'Biomass Fraction', '生物质部分', '13600', 'TERAJOULE', 'MEGAGRAM'],
            ['Rác thải đô thị - phần sinh khối', 'Municipal Wastes (Biomass Fraction)', '城市废物（生物质部分）', '0.0100', 'TERAJOULE', 'MEGAGRAM'],
            ['Municipal Solid Waste (MSW)', 'Municipal Solid Waste (MSW)', '城市固体废物', '0.0105', 'TERAJOULE', 'MEGAGRAM'],
            ['Nhựa', 'Plastics', '塑料', '0.036', 'TERAJOULE', 'MEGAGRAM'],
            ['Dệt may', 'Textiles', '纺织品', '0.015', 'TERAJOULE', 'MEGAGRAM'],
            ['Cao su', 'Rubber', '橡胶', '0.03', 'TERAJOULE', 'MEGAGRAM'],
            ['Tã lót', 'Nappies', '尿布', '0.0175', 'TERAJOULE', 'MEGAGRAM'],
            ['Rác thải công nghiệp', 'Industrial Solid Waste', '工业固体废物', '0.0115', 'TERAJOULE', 'MEGAGRAM'],
            ['Rác thải nguy hại', 'Hazardous Waste', '危险废物', NULL, NULL, NULL],
            ['Rác thải lâm sàng', 'Clinical Waste', '临床废物', '0.02', 'TERAJOULE', 'MEGAGRAM'],
            ['Bùn thải', 'Sewage Sludge', '污水污泥', '0.0125', 'TERAJOULE', 'MEGAGRAM']
            ];
        fuel_id UUID;
        i INT;
    BEGIN
        FOR i IN 1 .. array_length(fuel_names, 1) LOOP
                -- Insert into fuel table
                INSERT INTO fuel (version, name_vi, name_en, name_zh)
                VALUES (0, fuel_names[i][1], fuel_names[i][2], fuel_names[i][3])
                RETURNING id INTO fuel_id;

                INSERT INTO energy_conversion (version, fuel_id,
                                               conversion_value,
                                               conversion_unit_numerator,
                                               conversion_unit_denominator)
                VALUES (0, fuel_id,
                        fuel_names[i][4]::NUMERIC(20,6),
                        fuel_names[i][5],
                        fuel_names[i][6]
                       );
            END LOOP;
    END $$;

-- FACTOR
DO $$
    DECLARE
        factor_data    TEXT[][] := ARRAY [
            -- Format:
            -- [factor_name_vi, factor_name_en. factor_name_zh, co2 value, ch4 value, n2o value, unit_numerator, unit_denominator,emission_source_vi,is_direct_emission
            -- conversion_fuel_vi, conversion_value]
            ['Hệ số phát thải của than antraxit', 'Emission factor of anthracite coal', '无烟煤的排放因子', '98.300', '1', '1.5',
                'KILOGRAM', 'TERAJOULE', 'Công nghiệp năng lượng', 'Than antraxit', 'false'],
            ['Hệ số phát thải của than sub-bitum', 'Emission factor of sub-bituminous coal', '次烟煤的排放因子', '96.100', '1', '1.5',
                'KILOGRAM', 'TERAJOULE', 'Công nghiệp năng lượng', 'Than bán bitum', 'false'],
            ['Hệ số phát thải của dầu thô', 'Emission factor of crude oil', '原油的排放因子', '73.300', '3', '0.6', 'KILOGRAM',
                'TERAJOULE', 'Công nghiệp năng lượng', 'Dầu thô', 'false'],
            ['Hệ số phát thải của dầu diesel', 'Emission factor of diesel oil', '柴油的排放因子', '74.100', '3', '0.6', 'KILOGRAM',
                'TERAJOULE', 'Công nghiệp năng lượng', 'Dầu diesel', 'false'],
            ['Hệ số phát thải của dầu diesel (đường bộ)', 'Emission factor of diesel oil (road transport)', '柴油（公路运输）的排放因子',
                '74.100', '3.9', '3.9', 'KILOGRAM', 'TERAJOULE', 'Giao thông vận tải đường bộ', 'Dầu diesel', 'false'],
            ['Hệ số phát thải của xăng', 'Emission factor of gasoline', '汽油的排放因子', '69.300', '62', '0.2', 'KILOGRAM',
                'TERAJOULE', 'Giao thông vận tải đường bộ', 'Xăng động cơ', 'false'],
            ['Hệ số phát thải của than củi', 'Emission factor of charcoal', '木炭的排放因子', '112.000', '200', '4', 'KILOGRAM',
                'TERAJOULE', 'Công nghiệp năng lượng', 'Than củi', 'false'],
            ['Hệ số phát thải của xăng hàng không', 'Emission factor of aviation gasoline', '航空汽油的排放因子', '70.000', '0.5', '2',
                'KILOGRAM', 'TERAJOULE', 'Giao thông vận tải hàng không nội địa', 'Xăng hàng không', 'false'],
            ['Hệ số phát thải của dầu nhiên liệu', 'Emission factor of fuel oil', '燃油的排放因子', '77.400', '3', '0.6', 'KILOGRAM',
                'TERAJOULE', 'Công nghiệp năng lượng', 'Dầu nhiên liệu', 'false'],
            ['Hệ số phát thải của khí tự nhiên', 'Emission factor of natural gas', '天然气的排放因子', '56.100', '1', '0.1',
                'KILOGRAM', 'TERAJOULE', 'Công nghiệp năng lượng', 'Khí tự nhiên', 'false'],
            ['Hệ số phát thải của sinh khối', 'Emission factor of biomass', '生物质的排放因子', '100.000', '30', '4', 'KILOGRAM',
                'TERAJOULE', 'Công nghiệp năng lượng', 'Sinh khối', 'false'],
            ['Hệ số phát thải của than antraxit', 'Emission factor of anthracite coal', '无烟煤的排放因子', '98.3', '10', '1.5',
                'KILOGRAM', 'TERAJOULE', 'Công nghiệp sản xuất và xây dựng', 'Than antraxit', 'false'],
            ['Hệ số phát thải của dầu diesel', 'Emission factor of diesel oil', '柴油的排放因子', '74.100', '3', '0.6', 'KILOGRAM',
                'TERAJOULE', 'Công nghiệp sản xuất và xây dựng', 'Dầu diesel', 'false'],
            ['Hệ số phát thải của dầu nhiên liệu', 'Emission factor of fuel oil', '燃油的排放因子', '77.400', '3', '0.6', 'KILOGRAM',
                'TERAJOULE', 'Công nghiệp sản xuất và xây dựng', 'Dầu nhiên liệu', 'false'],
            ['Hệ số phát thải của khí hoả long', 'Emission factor of blast furnace gas', '高炉煤气的排放因子', '63.100', '1', '0.1',
                'KILOGRAM', 'TERAJOULE', 'Công nghiệp sản xuất và xây dựng', 'Khí dầu mỏ hóa lỏng', 'false'],
            ['Hệ số phát thải của khí tự nhiên', 'Emission factor of natural gas', '天然气的排放因子', '56.100', '1', '0.1',
                'KILOGRAM', 'TERAJOULE', 'Công nghiệp sản xuất và xây dựng', 'Khí tự nhiên', 'false'],
            ['Hệ số phát thải của sinh khối', 'Emission factor of biomass', '生物质的排放因子', '0', '30', '4', 'KILOGRAM',
                'TERAJOULE', 'Công nghiệp sản xuất và xây dựng', 'Sinh khối', 'false'],
            ['Hệ số phát thải của nhiên liệu hàng không', 'Emission factor of aviation fuel (Jet Kerosene)',
                '航空燃料（喷气煤油）的排放因子', '71.500', '0.5', '2', 'KILOGRAM', 'TERAJOULE', 'Giao thông vận tải hàng không nội địa',
                'Nhiên liệu phản lực', 'false'],
            ['Hệ số phát thải của dầu diesel', 'Emission factor of diesel oil', '柴油的排放因子', '74.100', '3.9', '3.9', 'KILOGRAM',
                'TERAJOULE', 'Giao thông vận tải đường bộ', 'Dầu diesel', 'false'],
            ['Hệ số phát thải của khí hoả long', 'Emission factor of blast furnace gas', '高炉煤气的排放因子', '63.100', '62', '0.2',
                'KILOGRAM', 'TERAJOULE', 'Giao thông vận tải đường bộ', 'Khí dầu mỏ hóa lỏng', 'false'],
            ['Hệ số phát thải của khí tự nhiên', 'Emission factor of natural gas', '天然气的排放因子', '56.100', '92', '3', 'KILOGRAM',
                'TERAJOULE', 'Giao thông vận tải đường bộ', 'Khí tự nhiên', 'false'],
            ['Hệ số phát thải của dầu diesel', 'Emission factor of diesel oil', '柴油的排放因子', '74.100', '4.15', '28.6',
                'KILOGRAM', 'TERAJOULE', 'Giao thông vận tải đường sắt', 'Dầu diesel', 'false'],
            ['Hệ số phát thải của dầu diesel', 'Emission factor of diesel oil', '柴油的排放因子', '74.100', '7', '2', 'KILOGRAM',
                'TERAJOULE', 'Giao thông vận tải đường thuỷ nội địa và hàng hải nội địa', 'Dầu diesel', 'false'],
            ['Hệ số phát thải của dầu nhiên liệu', 'Emission factor of fuel oil', '燃油的排放因子', '77.400', '7', '2', 'KILOGRAM',
                'TERAJOULE', 'Giao thông vận tải đường thuỷ nội địa và hàng hải nội địa', 'Dầu nhiên liệu', 'false'],
            ['Hệ số phát thải của than antraxit', 'Emission factor of anthracite coal', '无烟煤的排放因子', '98.3', '10', '1.5',
                'KILOGRAM', 'TERAJOULE', 'Thương mại và dịch vụ', 'Than antraxit', 'false'],
            ['Hệ số phát thải của dầu diesel', 'Emission factor of diesel oil', '柴油的排放因子', '74.100', '10', '0.6', 'KILOGRAM',
                'TERAJOULE', 'Thương mại và dịch vụ', 'Dầu diesel', 'false'],
            ['Hệ số phát thải của khí hoả long', 'Emission factor of blast furnace gas', '高炉煤气的排放因子', '63.100', '5', '0.1',
                'KILOGRAM', 'TERAJOULE', 'Thương mại và dịch vụ', 'Khí dầu mỏ hóa lỏng', 'false'],
            ['Hệ số phát thải của than củi', 'Emission factor of charcoal', '木炭的排放因子', '112.000', '200', '1', 'KILOGRAM',
                'TERAJOULE', 'Thương mại và dịch vụ', 'Than củi', 'false'],
            ['Hệ số phát thải của than antraxit', 'Emission factor of anthracite coal', '无烟煤的排放因子', '98.300', '300', '1.5',
                'KILOGRAM', 'TERAJOULE', 'Dân dụng', 'Than antraxit', 'false'],
            ['Hệ số phát thải của dầu hoả', 'Emission factor of kerosene', '煤油的排放因子', '71.900', '10', '0.6', 'KILOGRAM',
                'TERAJOULE', 'Dân dụng', 'Dầu hỏa', 'false'],
            ['Hệ số phát thải của khí hoả long', 'Emission factor of blast furnace gas', '高炉煤气的排放因子', '63.100', '5', '0.1',
                'KILOGRAM', 'TERAJOULE', 'Dân dụng', 'Khí dầu mỏ hóa lỏng', 'false'],
            ['Hệ số phát thải của sinh khối', 'Emission factor of biomass', '生物质的排放因子', '100.000', '300', '4', 'KILOGRAM',
                'TERAJOULE', 'Dân dụng', 'Rác thải đô thị - phần sinh khối', 'false'],
            ['Hệ số phát thải của xăng', 'Emission factor of gasoline', '汽油的排放因子', '69.300', '10', '0.6', 'KILOGRAM',
                'TERAJOULE', 'Nông nghiệp. lâm nghiệp và thuỷ sản', 'Xăng động cơ', 'false'],
            ['Hệ số phát thải của dầu diesel', 'Emission factor of diesel oil', '柴油的排放因子', '74.100', '10', '0.6', 'KILOGRAM',
                'TERAJOULE', 'Nông nghiệp. lâm nghiệp và thuỷ sản', 'Dầu diesel', 'false'],
            ['Hệ số phát thải của sinh khối', 'Emission factor of biomass', '生物质的排放因子', '0', '300', '4', 'KILOGRAM',
                'TERAJOULE', 'Nông nghiệp. lâm nghiệp và thuỷ sản', 'Sinh khối', 'false'],
            ['Hệ số phát tán trong khai thác than hầm lò', 'Emission factor in underground coal mining',
                '地下煤矿开采中的甲烷排放因子', '0', '16', '0', 'CUBIC_METER', 'MEGAGRAM', 'Khai thác than hầm lò', null, 'true'],
            ['Hệ số phát tán sau khai thác than hầm lò', 'Emission factor after underground coal mining', '地下煤矿开采后甲烷排放因子', '0',
                '0.1697', '0', 'CUBIC_METER', 'MEGAGRAM', 'Khai thác than hầm lò', null, 'true'],
            ['Hệ số phát tán trong khai thác than lộ thiên', 'Emission factor in surface coal mining', '露天煤矿开采中的甲烷排放因子', '0',
                '0.05375', '0', 'CUBIC_METER', 'MEGAGRAM', 'Khai thác than lộ thiên', null, 'true'],
            ['Hệ số phát tán sau khai thác than lộ thiên', 'Emission factor after surface coal mining', '露天煤矿开采后甲烷排放因子', '0',
                '0.1697', '0', 'CUBIC_METER', 'MEGAGRAM', 'Khai thác than lộ thiên', null, 'true'],
            ['Hệ số phát thải rò rỉ từ sản xuất dầu', 'Emission factor of leakage from oil production', '石油生产泄漏排放因子',
                '0.00215', '0.01035', '0', 'GIGAGRAM', 'THOUSAND_CUBIC_METER', 'Khai thác dầu', null, 'true'],
            ['Hệ số phát thải do đốt cháy tự nhiên từ sản xuất dầu', 'Emission factor of natural combustion from oil production', '石油生产自然燃烧排放因子',
                '0.0405', '0.000025', '0.00000064', 'GIGAGRAM', 'THOUSAND_CUBIC_METER', 'Khai thác dầu', null, 'true'],
            ['Hệ số phát thải phát tán trong sản xuất dầu', 'Emission factor of fugitive emissions in oil production', '石油生产中的无组织排放因子',
                '0.00249', '0.0196', '0', 'GIGAGRAM', 'THOUSAND_CUBIC_METER', 'Khai thác dầu', null, 'true'],
            ['Hệ số phát thải rò rỉ trong xử lý khí', 'Emission factor of leakage in gas processing', '天然气处理泄漏排放因子',
                '0.0675', '0', '0', 'GIGAGRAM', 'MILLION_CUBIC_METER', 'Khai thác khí tự nhiên', null, 'true'],
            ['Hệ số phát thải do đốt cháy tự nhiên từ sản xuất dầu', 'Emission factor of natural combustion from oil production', '石油生产自然燃烧排放因子',
                '0.00355', '0.00000024', '3.90E-08', 'GIGAGRAM', 'MILLION_CUBIC_METER', 'Khai thác khí tự nhiên', null, 'true'],
            ['Hệ số phát thải do đốt cháy tự nhiên từ sản xuất khí', 'Emission factor of natural combustion from gas production', '天然气生产自然燃烧排放因子',
                '0.0014', '0.00000088', '2.50E-08', 'GIGAGRAM', 'MILLION_CUBIC_METER', 'Khai thác khí tự nhiên', null, 'true'],
            ['Hệ số phát thải phát tán trong sản xuất khí', 'Emission factor of fugitive emissions in gas production', '天然气生产中的无组织排放因子',
                '0.000097', '0.01219', '0', 'GIGAGRAM', 'MILLION_CUBIC_METER', 'Khai thác khí tự nhiên', null, 'true'],
            ['Hệ số phát thải phát tán trong xử lý khí', 'Emission factor of fugitive emissions in gas processing', '天然气处理中的无组织排放因子',
                '0.00025', '0.00079', '0', 'GIGAGRAM', 'MILLION_CUBIC_METER THÔ ĐẦU VÀO', 'Khai thác khí tự nhiên', null, 'true']
            ];
        source_id      UUID;
        conversion_id  UUID;
        i              INT;
    BEGIN
        FOR i IN 1 .. array_length(factor_data, 1)
            LOOP
                SELECT id
                INTO source_id
                FROM emission_source
                WHERE name_vi = factor_data[i][9];

                IF source_id IS NULL THEN
                    RAISE EXCEPTION 'No matching emission_source found for name_vi: %', factor_data[i][9];
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
                                             energy_conversion_id)
                VALUES (0, 'seed_data_sql', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'seed_data_sql',
                        factor_data[i][4]::DECIMAL(20, 6), factor_data[i][5]::DECIMAL(20, 6), factor_data[i][6]::DECIMAL(20, 6),
                        factor_data[i][1], factor_data[i][2], factor_data[i][3],
                        factor_data[i][7], factor_data[i][8],
                        source_id,
                        '',
                        '2025-01-01 00:00:00',
                        '2030-01-01 00:00:00',
                        factor_data[i][11]::BOOLEAN,
                        conversion_id);
            END LOOP;
    END
$$;

