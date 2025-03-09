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
            ['Sử dụng điện', 'Electricity Use', '电力使用']
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
            ['Etan', 'Ethane', '乙烷', '47.5', 'TERAJOULE', 'GIGAGRAM'],
            ['Propan', 'Propane', '丙烷', '46.3', 'TERAJOULE', 'GIGAGRAM'],
            ['Butan', 'Butane', '丁烷', '45.5', 'TERAJOULE', 'GIGAGRAM'],
            ['Xăng động cơ', 'Motor Gasoline', '车用汽油', '44.3', 'TERAJOULE', 'THOUSAND_CUBIC_METER'],
            ['Xăng hàng không', 'Aviation Gasoline', '航空汽油', '44.3', 'TERAJOULE', 'THOUSAND_CUBIC_METER'],
            ['Xăng phản lực', 'Jet Gasoline', '喷气汽油', '44.1', 'TERAJOULE', 'THOUSAND_CUBIC_METER'],
            ['Nhiên liệu phản lực', 'Jet Kerosene', '喷气煤油', '44.1', 'TERAJOULE', 'THOUSAND_CUBIC_METER'],
            ['Dầu hỏa khác', 'Other Kerosene', '其他煤油', '43.8', 'TERAJOULE', 'THOUSAND_CUBIC_METER'],
            ['Dầu đá phiến', 'Shale Oil', '页岩油', '40.2', 'TERAJOULE', 'GIGAGRAM'],
            ['Dầu diesel', 'Gas Diesel Oil', '燃气柴油', '43', 'TERAJOULE', 'THOUSAND_CUBIC_METER'],
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

