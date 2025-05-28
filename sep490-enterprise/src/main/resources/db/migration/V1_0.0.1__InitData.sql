INSERT INTO credit_packages (created_date, created_by, last_modified_date, last_modified_by, id, version, active)
VALUES ('2025-05-28 19:17:50.272554', 'admin', '2025-05-28 19:17:50.272554', 'admin', '8c3a84fe-7f4e-4d70-8f83-765b47c81b30', 0,
        true);
INSERT INTO credit_packages (created_date, created_by, last_modified_date, last_modified_by, id, version, active)
VALUES ('2025-05-28 19:17:50.272554', 'admin', '2025-05-28 19:17:50.272554', 'admin', '4e871690-9e94-427d-88bc-e25c81792aab', 0,
        true);
INSERT INTO credit_packages (created_date, created_by, last_modified_date, last_modified_by, id, version, active)
VALUES ('2025-05-28 19:17:50.272554', 'admin', '2025-05-28 19:17:50.272554', 'admin', '69152ed0-8f55-4abc-9c6a-abc4cd13ae4a', 0,
        false);
INSERT INTO credit_packages (created_date, created_by, last_modified_date, last_modified_by, id, version, active)
VALUES ('2025-05-28 19:17:50.272554', 'admin', '2025-05-28 19:17:50.272554', 'admin', '4293f414-b697-4b63-84de-c2c98501edcd', 0,
        true);

INSERT INTO credit_packages_versions (price, number_of_credits, id, version, credit_package_id, active, discount)
VALUES (15000, 500, 'c274f0bf-b9d1-4aab-888b-a61a7af0cfcd', 0, '4293f414-b697-4b63-84de-c2c98501edcd', true, 0);
INSERT INTO credit_packages_versions (price, number_of_credits, id, version, credit_package_id, active, discount)
VALUES (1000000, 100, '4e84e83e-6097-4fa4-be98-6c942d2f9d39', 0, '8c3a84fe-7f4e-4d70-8f83-765b47c81b30', true, 0);
INSERT INTO credit_packages_versions (price, number_of_credits, id, version, credit_package_id, active, discount)
VALUES (2000000, 300, '6c538e8c-bc05-48c8-8398-2441109fb46b', 0, '69152ed0-8f55-4abc-9c6a-abc4cd13ae4a', false, 0);
INSERT INTO credit_packages_versions (price, number_of_credits, id, version, credit_package_id, active, discount)
VALUES (100000, 250, 'a40db536-31ff-49b9-b5b9-51679af1172b', 0, '4e871690-9e94-427d-88bc-e25c81792aab', true, 0);
INSERT INTO credit_packages_versions (price, number_of_credits, id, version, credit_package_id, active, discount)
VALUES (100000, 100, '7db0e227-d88d-4223-a547-4e52f7167a95', 0, '4e871690-9e94-427d-88bc-e25c81792aab', false, 0);
INSERT INTO credit_packages_versions (price, number_of_credits, id, version, credit_package_id, active, discount)
VALUES (4000000, 400, 'e77dab23-6d35-4f54-8462-0d855d18bf0b', 0, '69152ed0-8f55-4abc-9c6a-abc4cd13ae4a', true, 0);
INSERT INTO credit_packages_versions (price, number_of_credits, id, version, credit_package_id, active, discount)
VALUES (2000000, 400, '205b1da4-8291-4cb1-885e-2d2db2940dba', 0, '69152ed0-8f55-4abc-9c6a-abc4cd13ae4a', false, 0);

INSERT INTO credit_convert_ratio (created_date, created_by, last_modified_date, last_modified_by, id, version, ratio,
                                  convert_type)
VALUES ('2025-05-28 19:17:50.174935', 'sql_by_admin', '2025-05-28 19:17:50.174935', 'sql_by_admin',
        '4894a2b5-7022-48b2-b809-fe90fa9d078d', 0, 1, 'MONTH');
INSERT INTO credit_convert_ratio (created_date, created_by, last_modified_date, last_modified_by, id, version, ratio,
                                  convert_type)
VALUES ('2025-05-28 19:17:50.174935', 'sql_by_admin', '2025-05-28 19:17:50.174935', 'sql_by_admin',
        'f858aec1-1558-434e-b751-074f6ea95604', 0, 0.1, 'DEVICE');


INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('00cfaeee-6195-4c8a-a7e4-2ab29b9e65a8', 0, 'Dầu thô', 'Crude Oil', '原油');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('1db75192-e5dc-4017-a1d6-fe3cca699a5a', 0, 'Orimulsion', 'Orimulsion', '奥里乳化油');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('d3a049c2-8d74-4b3b-b51c-869fac0abeff', 0, 'Khí tự nhiên lỏng', 'Natural Gas Liquids', '天然气液体');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('d24b4563-6ecb-47b6-9e61-04078899876a', 0, 'Khí tự nhiên', 'Natural Gas', '天然气');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('ccf5110e-b54d-4659-95a1-7298eacc47cb', 0, 'Etan', 'Ethane', '乙烷');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('0f719e26-b9e6-469f-8099-5ede041db854', 0, 'Propan', 'Propane', '丙烷');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('41d1aa7b-9c55-4099-9404-f59f0500d1a3', 0, 'Butan', 'Butane', '丁烷');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('2204009a-6864-494c-9cb5-93fe92a3a439', 0, 'Xăng động cơ', 'Motor Gasoline', '车用汽油');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('bd35fdbe-84a5-46ae-b6cb-0e13dc47fb3f', 0, 'Xăng hàng không', 'Aviation Gasoline', '航空汽油');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('9c012024-665d-4e79-a427-b1227b7733e5', 0, 'Xăng phản lực', 'Jet Gasoline', '喷气汽油');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('842a3e0d-31b5-4922-a140-b9aef788c268', 0, 'Nhiên liệu phản lực', 'Jet Kerosene', '喷气煤油');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('b610516c-05df-4e55-a51e-aa72c097af52', 0, 'Dầu hỏa', 'Kerosene', '煤油');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('65a1cd0e-b3c4-42e8-b679-276526affaf9', 0, 'Dầu hỏa khác', 'Other Kerosene', '其他煤油');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('460b6e46-7b3d-4c53-a436-1cf7be3d0e3c', 0, 'Dầu đá phiến', 'Shale Oil', '页岩油');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('a18dcc80-e40b-406a-b181-df77cd612cae', 0, 'Dầu diesel', 'Gas Diesel Oil', '燃气柴油');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('9b5aa3a8-5888-497a-8999-dd6f577b941c', 0, 'Dầu nhiên liệu', 'Fuel Oil', '燃料油');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('9a481a2f-81b5-4ab4-b1fa-726d2e28b8f6', 0, 'Dầu nhiên liệu cặn', 'Residual Fuel Oil', '残余燃料油');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('1beaa465-f640-47fc-a267-2265df234d47', 0, 'Khí dầu mỏ hóa lỏng', 'Liquefied Petroleum Gas', '液化石油气');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('67bb6b85-59bb-4a90-b117-f192de06b36c', 0, 'Naphta', 'Naphtha', '石脑油');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('b8f1d747-3e2b-4cb0-ac2c-848cc47b92fb', 0, 'Khí tự nhiên', 'Natural Gas', '天然气');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('4d10ce74-a5ea-4df2-9d0a-cbe484e3909b', 0, 'Dầu bôi trơn', 'Lubricants', '润滑油');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('1e6356bf-cf07-48b8-91c3-3cac1604a1a9', 0, 'Than cốc dầu mỏ', 'Petroleum Coke', '石油焦');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('be2f41ee-d237-48b4-bc97-317fef12a3fc', 0, 'Nguyên liệu lọc dầu', 'Refinery Feedstocks', '炼油原料');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('091bdc3e-394f-4956-b65d-7578c1650b31', 0, 'Khí nhà máy lọc dầu', 'Refinery Gas', '炼油厂气体');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('b3768f1b-88a8-463e-811c-035f38aa9d3f', 0, 'Sáp paraffin', 'Paraffin Waxes', '石蜡');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('631dded6-a3c0-471d-b735-861100906e14', 0, 'Sản phẩm dầu mỏ khác', 'Other Petroleum Products', '其他石油产品');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('be90f7a5-0506-4e89-8dac-19e0e77eb062', 0, 'Than antraxit', 'Anthracite', '无烟煤');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('b632dc50-f289-4ee0-9cb9-fc5f19d503bf', 0, 'Than cốc', 'Coking Coal', '焦煤');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('f2b95740-d19e-4131-9c32-f40a7e1df279', 0, 'Than bitum khác', 'Other Bituminous Coal', '其他烟煤');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('5e74532d-da8f-42e0-a32f-fb42c0995c2d', 0, 'Than bán bitum', 'Sub-bituminous Coal', '次烟煤');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('2efbbd96-40d2-45e2-b89a-9adec3aca9dc', 0, 'Than non', 'Lignite', '褐煤');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('37cd4dea-9ce5-4a26-9e58-8d1a549294d3', 0, 'Dầu đá phiến và cát dầu', 'Oil Shale and Tar Sands', '油页岩和油砂');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('bf550f4d-204d-4ae2-be39-45a50c54fdee', 0, 'Than non ép viên', 'Brown Coal Briquettes', '褐煤球');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('ed71dcd5-2eb8-4223-b54e-5761b73a1180', 0, 'Nhiên liệu ép', 'Patent Fuel', '专利燃料');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('77e4f07e-8205-49e1-9075-c595a8e0a88e', 0, 'Than cốc lò luyện / Than cốc từ than non', 'Coke Oven Coke / Lignite Coke',
        '焦炉焦炭 / 褐煤焦炭');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('759278a2-0a3c-4434-b923-4eab78a847f0', 0, 'Than cốc', 'Gas Coke', '煤气焦炭');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('b6ddcefe-c43a-4e92-a15b-7cd31ba2c30c', 0, 'Hắc ín than', 'Coal Tar', '煤焦油');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('a60e2dd1-d854-4896-985f-36a6e9142ad3', 0, 'Khí nhà máy khí', 'Gas Works Gas', '煤气厂气体');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('4cb30303-e8eb-4547-b68a-cd38bdfa0e39', 0, 'Khí lò cốc', 'Coke Oven Gas', '焦炉煤气');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('2e59ea6f-f911-49d2-b590-cfd6579ef5e3', 0, 'Khí lò cao', 'Blast Furnace Gas', '高炉煤气');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('9e7e6de9-e2cf-4a23-bf04-fc8f5718f603', 0, 'Khí lò luyện thép oxy', 'Oxygen Steel Furnace Gas', '氧气转炉煤气');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('36c18190-bdb4-4129-b202-25e0e39fa690', 0, 'Khí tự nhiên (khô)', 'Natural Gas (Dry)', '天然气（干）');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('9c1b327c-91b4-466b-a1f7-7234a71452b6', 0, 'Rác thải đô thị - phần không sinh khối',
        'Municipal Wastes (Non-Biomass Fraction)', '城市废物（非生物质部分）');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('e18e3106-5872-4b90-8235-5919033d2408', 0, 'Rác thải công nghiệp', 'Industrial Wastes', '工业废物');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('bb67b71c-6a68-48b6-b9af-18fe112a5f25', 0, 'Dầu thải', 'Waste Oils', '废油');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('5f9b341a-264e-452b-96d6-17851b1fed29', 0, 'Than bùn', 'Peat', '泥炭');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('7d534e3a-30bf-43eb-8c76-294d2cefde38', 0, 'Gỗ / Phế thải gỗ', 'Wood / Wood Waste', '木材 / 木材废料');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('123bebca-3b8f-4d29-99b1-184bfef32aab', 0, 'Dung dịch sunphit', 'Sulphite Lyes', '亚硫酸盐液');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('f9bae3ee-1ba2-4fbd-9d30-e43c101b6f4e', 0, 'Sinh khối rắn sơ cấp khác', 'Other Primary Solid Biomass',
        '其他初级固体生物质');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('d73cfb63-1117-4d26-ba59-a240e5184391', 0, 'Than củi', 'Charcoal', '木炭');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('20074ddc-e2ce-44be-8e1e-a9070df02f17', 0, 'Xăng sinh học', 'Biogasoline', '生物汽油');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('2abe840c-1a44-40b3-b9aa-4e137ab4dc3f', 0, 'Diesel sinh học', 'Biodiesels', '生物柴油');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('13b9bd26-b35f-49cd-8286-044cb1d89405', 0, 'Nhiên liệu sinh học lỏng khác', 'Other Liquid Biofuels', '其他液体生物燃料');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('02a30f3e-bf8f-4628-aa6f-2958631137ec', 0, 'Khí bãi rác', 'Landfill Gas', '垃圾填埋气');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('f8ff0f43-6535-4147-8558-bd274355cf92', 0, 'Khí bùn', 'Sludge Gas', '污泥气');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('e577f092-a9ad-4057-acc8-63fa869e7878', 0, 'Khí sinh học khác', 'Other Biogas', '其他沼气');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('465fca60-8c15-4762-a37c-0d67a7ab087e', 0, 'Sinh khối', 'Biomass Fraction', '生物质部分');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('527b7c02-0c6d-4b3a-917d-4a405341d4dc', 0, 'Rác thải đô thị - phần sinh khối', 'Municipal Wastes (Biomass Fraction)',
        '城市废物（生物质部分）');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('d4ddb9cd-6a35-4134-b114-431fac15e0d0', 0, 'Municipal Solid Waste (MSW)', 'Municipal Solid Waste (MSW)', '城市固体废物');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('4ab5ea96-5df4-4386-9e6e-820838e2a875', 0, 'Nhựa', 'Plastics', '塑料');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('96b37ef4-6c8c-4153-9597-96e358cfc7aa', 0, 'Dệt may', 'Textiles', '纺织品');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('95145eb8-7161-4c28-8a6a-56ce61bc53b9', 0, 'Cao su', 'Rubber', '橡胶');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('d1d39227-1761-472c-8496-2dec73243771', 0, 'Tã lót', 'Nappies', '尿布');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('d70eb12e-6f33-4c9e-b52e-e1c924c38083', 0, 'Rác thải công nghiệp', 'Industrial Solid Waste', '工业固体废物');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('e6b324b9-4f12-4c09-8bcf-acb0eb0cac43', 0, 'Rác thải nguy hại', 'Hazardous Waste', '危险废物');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('e62b3bda-8fc7-4e14-b817-d088ae305de5', 0, 'Rác thải lâm sàng', 'Clinical Waste', '临床废物');
INSERT INTO fuel (id, version, name_vi, name_en, name_zh)
VALUES ('0c3c9895-773c-4147-9a49-aee42854406f', 0, 'Bùn thải', 'Sewage Sludge', '污水污泥');


INSERT INTO emission_source (id, version, name_vi, name_en, name_zh)
VALUES ('ba9dc6de-926e-4105-93c7-0f4a550fdab7', 0, 'Công nghiệp năng lượng', 'Energy Industry', '能源工业');
INSERT INTO emission_source (id, version, name_vi, name_en, name_zh)
VALUES ('ac6d6897-5702-425d-8c24-e83b8f7dbd34', 0, 'Công nghiệp sản xuất và xây dựng', 'Manufacturing and Construction Industry',
        '制造与建筑工业');
INSERT INTO emission_source (id, version, name_vi, name_en, name_zh)
VALUES ('ca595d21-8b19-4b0c-8d88-4f48c1fe7606', 0, 'Giao thông vận tải hàng không nội địa', 'Domestic Air Transport',
        '国内航空运输');
INSERT INTO emission_source (id, version, name_vi, name_en, name_zh)
VALUES ('0a0d6d52-ad15-400a-8a43-5e2993757dad', 0, 'Giao thông vận tải đường bộ', 'Road Transport', '公路运输');
INSERT INTO emission_source (id, version, name_vi, name_en, name_zh)
VALUES ('81264f0b-87ea-4ad3-9661-442e93c48041', 0, 'Giao thông vận tải đường sắt', 'Rail Transport', '铁路运输');
INSERT INTO emission_source (id, version, name_vi, name_en, name_zh)
VALUES ('1161d039-0831-4375-b9a4-24695ecae65b', 0, 'Giao thông vận tải đường thuỷ nội địa và hàng hải nội địa',
        'Inland Waterway and Domestic Maritime Transport', '内河及国内海上运输');
INSERT INTO emission_source (id, version, name_vi, name_en, name_zh)
VALUES ('e1884bed-7223-48a7-9568-b53d0b133727', 0, 'Thương mại và dịch vụ', 'Commerce and Services', '商业与服务');
INSERT INTO emission_source (id, version, name_vi, name_en, name_zh)
VALUES ('a0151298-d6cf-493a-ad1b-8ab1db8de881', 0, 'Dân dụng', 'Residential', '民用');
INSERT INTO emission_source (id, version, name_vi, name_en, name_zh)
VALUES ('3e87961d-f6b3-43cc-9b98-56c7fdfe6b01', 0, 'Nông nghiệp, lâm nghiệp và thuỷ sản', 'Agriculture, Forestry, and Fisheries',
        '农业、林业和渔业');
INSERT INTO emission_source (id, version, name_vi, name_en, name_zh)
VALUES ('0de5518d-8f3e-4975-be35-21dda2b15925', 0, 'Khai thác than hầm lò', 'Underground Coal Mining', '地下煤炭开采');
INSERT INTO emission_source (id, version, name_vi, name_en, name_zh)
VALUES ('779af8bc-6b7c-4845-8a9c-f6c608d0be47', 0, 'Khai thác dầu', 'Oil Extraction', '石油开采');
INSERT INTO emission_source (id, version, name_vi, name_en, name_zh)
VALUES ('75c1d81d-8b4f-4fac-b3a1-ffbaa3f7ea7f', 0, 'Khai thác khí tự nhiên', 'Natural Gas Extraction', '天然气开采');
INSERT INTO emission_source (id, version, name_vi, name_en, name_zh)
VALUES ('b49cff33-2eb2-4bde-88ca-906597294c6a', 0, 'Phát thải từ bãi chôn lấp chất thải rắn',
        'Emissions from Solid Waste Landfills', '固体废物填埋场排放');
INSERT INTO emission_source (id, version, name_vi, name_en, name_zh)
VALUES ('23b5abbb-f546-4024-8674-f03c318f0500', 0, 'Xử lý chất thải rắn bằng phương pháp sinh học',
        'Biological Solid Waste Treatment', '生物固体废物处理');
INSERT INTO emission_source (id, version, name_vi, name_en, name_zh)
VALUES ('eb6614e7-ff4b-478c-b9ee-061b6cf6258b', 0, 'Thiêu đốt chất thải (chất thải rắn sinh hoạt)',
        'Incineration of Waste (Household Solid Waste)', '焚烧废物（生活固体废物）');
INSERT INTO emission_source (id, version, name_vi, name_en, name_zh)
VALUES ('86fe5fc5-1225-42f8-ac53-1f1addd2cfc8', 0, 'Thiêu đốt chất thải', 'Waste Incineration', '废物焚烧');
INSERT INTO emission_source (id, version, name_vi, name_en, name_zh)
VALUES ('317aad93-e942-456f-aaca-bb23ad8dac24', 0, 'Thiêu đốt chất thải - Lò đốt vỉ động liên tục',
        'Waste Incineration - Continuous Grate Incinerator', '废物焚烧 - 连续炉排焚烧炉');
INSERT INTO emission_source (id, version, name_vi, name_en, name_zh)
VALUES ('670b7abb-fda1-49db-aa2a-c09ae047be7c', 0, 'Thiêu đốt chất thải - Lò đốt tầng sôi liên tục',
        'Waste Incineration - Continuous Fluidized Bed Incinerator', '废物焚烧 - 连续流化床焚烧炉');
INSERT INTO emission_source (id, version, name_vi, name_en, name_zh)
VALUES ('950d08aa-3d82-4fb1-87ab-97c0c1469f4c', 0, 'Thiêu đốt chất thải - Lò đốt vỉ động bán liên tục',
        'Waste Incineration - Semi-Continuous Grate Incinerator', '废物焚烧 - 半连续炉排焚烧炉');
INSERT INTO emission_source (id, version, name_vi, name_en, name_zh)
VALUES ('8f798bbf-2edf-4ece-a66f-0f9a8df0828d', 0, 'Thiêu đốt chất thải - Lò đốt tầng sôi bán liên tục',
        'Waste Incineration - Semi-Continuous Fluidized Bed Incinerator', '废物焚烧 - 半连续流化床焚烧炉');
INSERT INTO emission_source (id, version, name_vi, name_en, name_zh)
VALUES ('84021996-361f-43cb-b99a-f7edf6b87f16', 0, 'Thiêu đốt chất thải - Lò đốt vỉ động - đốt hàng loạt',
        'Waste Incineration - Batch Grate Incinerator', '废物焚烧 - 批次炉排焚烧炉');
INSERT INTO emission_source (id, version, name_vi, name_en, name_zh)
VALUES ('4da98ca0-473f-4252-984f-6bc9c61aae42', 0, 'Thiêu đốt chất thải - Lò đốt tầng sôi - đốt hàng loạt',
        'Waste Incineration - Batch Fluidized Bed Incinerator', '废物焚烧 - 批次流化床焚烧炉');
INSERT INTO emission_source (id, version, name_vi, name_en, name_zh)
VALUES ('31dc71b8-c19e-4363-8df0-d0f75430f21b', 0, 'Đốt lộ thiên chất thải', 'Open Burning of Waste', '露天焚烧废物');
INSERT INTO emission_source (id, version, name_vi, name_en, name_zh)
VALUES ('7776a811-2367-4d58-a8d2-cc7b5ab878c2', 0, 'Xử lý và xả nước thải sinh hoạt',
        'Domestic Wastewater Treatment and Discharge', '生活污水处理与排放');
INSERT INTO emission_source (id, version, name_vi, name_en, name_zh)
VALUES ('1d59fd19-6389-496e-be59-146878c8f491', 0, 'Xử lý và xả nước thải công nghiệp',
        'Industrial Wastewater Treatment and Discharge', '工业污水处理与排放');
INSERT INTO emission_source (id, version, name_vi, name_en, name_zh)
VALUES ('536d72ea-3fa3-4253-8f49-8ba240b1bffc', 0, 'Sử dụng điện', 'Electricity Use', '电力使用');
INSERT INTO emission_source (id, version, name_vi, name_en, name_zh)
VALUES ('16563e66-baf8-4fb5-8719-2b4cecfd94a8', 0, 'Khai thác than lộ thiên', 'Open-pit coal mining', '露天煤矿开采');
INSERT INTO emission_source (id, version, name_vi, name_en, name_zh)
VALUES ('c1dae190-34c1-4462-91e2-2f1fa5b0a782', 0, 'Nông nghiệp. lâm nghiệp và thuỷ sản', 'Agriculture, forestry, and fisheries',
        '农业、林业和渔业');
INSERT INTO emission_source (id, version, name_vi, name_en, name_zh)
VALUES ('62f3128e-e2dc-4cdf-ac07-b072f9908c30', 0, 'Tiêu thụ điện', 'Electricity consumption', '电力消耗');


INSERT INTO chemical_density (id, version, chemical_formula, value, unit_numerator, unit_denominator)
VALUES ('c42194fc-6245-432f-9a87-894b5807d517', 1, 'CO2', 1.977, 'KILOGRAM', 'CUBIC_METER');
INSERT INTO chemical_density (id, version, chemical_formula, value, unit_numerator, unit_denominator)
VALUES ('39293687-73e5-4e6f-aa2f-0b0751c09d69', 1, 'CH4', 0.717, 'KILOGRAM', 'CUBIC_METER');
INSERT INTO chemical_density (id, version, chemical_formula, value, unit_numerator, unit_denominator)
VALUES ('d522d260-808b-4d6b-a05b-c953bfaa5c90', 1, 'N2O', 1.978, 'KILOGRAM', 'CUBIC_METER');


INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('ac6a9b08-010f-41e3-9521-40abbae51084', 0, '00cfaeee-6195-4c8a-a7e4-2ab29b9e65a8', 44.500000, 'TERAJOULE', 'GIGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('634ee48c-aa74-4ddd-8abb-2fee92f8959a', 0, '1db75192-e5dc-4017-a1d6-fe3cca699a5a', 27.500000, 'TERAJOULE', 'GIGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('58fb37d5-64f3-4220-8d21-7bd3a33ba068', 0, 'd3a049c2-8d74-4b3b-b51c-869fac0abeff', null, null, null);
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('a940ef6c-3b1d-4f5f-bc50-ca32f82a4faa', 0, 'd24b4563-6ecb-47b6-9e61-04078899876a', 0.037683, 'TERAJOULE', 'GIGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('65087064-2764-4561-8f11-e7c520f13e70', 0, 'ccf5110e-b54d-4659-95a1-7298eacc47cb', 47.500000, 'TERAJOULE', 'GIGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('8cf71173-2482-43cb-84f8-e16543dd28c3', 0, '0f719e26-b9e6-469f-8099-5ede041db854', 46.300000, 'TERAJOULE', 'GIGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('288bedff-69de-4089-90c9-660d5ba50e38', 0, '41d1aa7b-9c55-4099-9404-f59f0500d1a3', 45.500000, 'TERAJOULE', 'GIGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('1161e23b-b7be-402b-8987-024889ab3565', 0, '2204009a-6864-494c-9cb5-93fe92a3a439', 44.300000, 'TERAJOULE',
        'THOUSAND_CUBIC_METER');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('50a6c2d2-603f-4621-bb81-5d07d4dfd7c9', 0, 'bd35fdbe-84a5-46ae-b6cb-0e13dc47fb3f', 44.300000, 'TERAJOULE',
        'THOUSAND_CUBIC_METER');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('3c2a15bf-c7ec-41de-95a7-c3d1fb80cc36', 0, '9c012024-665d-4e79-a427-b1227b7733e5', 44.100000, 'TERAJOULE',
        'THOUSAND_CUBIC_METER');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('78227fc9-2191-4dd9-8029-1b266416bee9', 0, '842a3e0d-31b5-4922-a140-b9aef788c268', 44.100000, 'TERAJOULE',
        'THOUSAND_CUBIC_METER');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('c99109bf-baee-46db-a49a-90293237a046', 0, 'b610516c-05df-4e55-a51e-aa72c097af52', 34.400000, 'TERAJOULE',
        'THOUSAND_CUBIC_METER');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('03da87f7-8aea-42f2-b9ec-b8276793a29b', 0, '65a1cd0e-b3c4-42e8-b679-276526affaf9', 43.800000, 'TERAJOULE',
        'THOUSAND_CUBIC_METER');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('33dce7f0-9ad2-4b88-8f7e-4bf5899a3f5e', 0, '460b6e46-7b3d-4c53-a436-1cf7be3d0e3c', 40.200000, 'TERAJOULE', 'GIGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('7794d70a-7d7d-46f5-aa09-d3902bb360d7', 0, 'a18dcc80-e40b-406a-b181-df77cd612cae', 43.000000, 'TERAJOULE',
        'THOUSAND_CUBIC_METER');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('ab8a8af6-56e2-49f8-8e96-c0d2aae90aad', 0, '9b5aa3a8-5888-497a-8999-dd6f577b941c', 39.358000, 'TERAJOULE',
        'THOUSAND_CUBIC_METER');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('eac9217f-4334-4483-a0af-86a7aeda0ed8', 0, '9a481a2f-81b5-4ab4-b1fa-726d2e28b8f6', 40.400000, 'TERAJOULE', 'GIGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('bd30dfa7-5f82-45ba-a29c-342ce02efd5c', 0, '1beaa465-f640-47fc-a267-2265df234d47', 47.300000, 'TERAJOULE', 'GIGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('861cbedf-ad8c-4da7-92ff-36213a78701a', 0, '67bb6b85-59bb-4a90-b117-f192de06b36c', 44.500000, 'TERAJOULE',
        'THOUSAND_CUBIC_METER');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('dd7176a1-0b31-478c-b465-62807ebc48bf', 0, 'b8f1d747-3e2b-4cb0-ac2c-848cc47b92fb', 39.900000, 'TERAJOULE',
        'MILLION_CUBIC_METER');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('6bb0585e-d2ac-42c4-aaf0-a725c733185e', 0, '4d10ce74-a5ea-4df2-9d0a-cbe484e3909b', 40.200000, 'TERAJOULE', 'GIGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('d62c0f68-53bb-40ed-93d4-1e12b7d6c35f', 0, '1e6356bf-cf07-48b8-91c3-3cac1604a1a9', 32.500000, 'TERAJOULE', 'GIGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('92012681-25aa-48a0-922e-7484c1b36758', 0, 'be2f41ee-d237-48b4-bc97-317fef12a3fc', 43.300000, 'TERAJOULE', 'GIGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('6c792e9f-2dbe-4a36-8eca-ee21a5f5d492', 0, '091bdc3e-394f-4956-b65d-7578c1650b31', 44.500000, 'TERAJOULE', 'GIGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('23f70f85-e965-4923-b31c-35cb5ba50566', 0, 'b3768f1b-88a8-463e-811c-035f38aa9d3f', 40.000000, 'TERAJOULE', 'GIGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('f1a0fa55-e404-4873-a1ee-c22473670b8d', 0, '631dded6-a3c0-471d-b735-861100906e14', 41.000000, 'TERAJOULE', 'GIGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('ce5c8d55-4010-4168-b90c-808436d9fd32', 0, 'be90f7a5-0506-4e89-8dac-19e0e77eb062', 30.000000, 'TERAJOULE', 'GIGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('8e521050-ecb7-4d30-9286-b73d4868eaf6', 0, 'b632dc50-f289-4ee0-9cb9-fc5f19d503bf', 28.200000, 'TERAJOULE', 'GIGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('6cf135d5-543c-4cf9-85a9-6bb28fa2f097', 0, 'f2b95740-d19e-4131-9c32-f40a7e1df279', 25.800000, 'TERAJOULE', 'GIGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('019868f7-0da4-4771-a8bd-6cb7a99bfc13', 0, '5e74532d-da8f-42e0-a32f-fb42c0995c2d', 18.900000, 'TERAJOULE', 'GIGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('af225669-87c1-4cb5-a30e-5b0a045be842', 0, '2efbbd96-40d2-45e2-b89a-9adec3aca9dc', 11.900000, 'TERAJOULE', 'GIGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('483677d3-fd7f-4fc4-8c85-ef0536820a7d', 0, '37cd4dea-9ce5-4a26-9e58-8d1a549294d3', 8.900000, 'TERAJOULE', 'GIGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('35e9cd13-34b2-4ba7-94b8-4e220e81c667', 0, 'bf550f4d-204d-4ae2-be39-45a50c54fdee', 20.000000, 'TERAJOULE', 'GIGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('9b18c542-f473-4ea2-b98d-976b1bd10990', 0, 'ed71dcd5-2eb8-4223-b54e-5761b73a1180', 0.020000, 'TERAJOULE', 'MEGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('55bc5256-e159-435c-9460-6772c51a5b5a', 0, '77e4f07e-8205-49e1-9075-c595a8e0a88e', 0.028200, 'TERAJOULE', 'MEGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('5ee70282-7ed5-48e2-9b32-23892735f833', 0, '759278a2-0a3c-4434-b923-4eab78a847f0', 0.028500, 'TERAJOULE', 'MEGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('0a426bb7-fafc-430d-a57c-ba4750f9698f', 0, 'b6ddcefe-c43a-4e92-a15b-7cd31ba2c30c', 0.032600, 'TERAJOULE', 'MEGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('babd3894-ed4a-48dc-b786-0123e6d13fea', 0, 'a60e2dd1-d854-4896-985f-36a6e9142ad3', 0.017600, 'TERAJOULE',
        'THOUSAND_CUBIC_METER');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('71717fe7-6786-4ca9-b5f9-d7a4b331d253', 0, '4cb30303-e8eb-4547-b68a-cd38bdfa0e39', 0.017600, 'TERAJOULE',
        'THOUSAND_CUBIC_METER');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('ab43b3ab-f979-4eec-9c8d-167398400167', 0, '2e59ea6f-f911-49d2-b590-cfd6579ef5e3', 0.003600, 'TERAJOULE',
        'THOUSAND_CUBIC_METER');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('30142741-8c28-423e-b6d7-0b2b2628f5d4', 0, '9e7e6de9-e2cf-4a23-bf04-fc8f5718f603', 0.007000, 'TERAJOULE',
        'THOUSAND_CUBIC_METER');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('6b17d900-a3d6-4ba8-b286-875718707cab', 0, '36c18190-bdb4-4129-b202-25e0e39fa690', 0.048000, 'TERAJOULE', 'MEGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('ac938d0c-bc26-40ba-9719-9a92d360cf67', 0, '9c1b327c-91b4-466b-a1f7-7234a71452b6', 0.010000, 'TERAJOULE', 'MEGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('dd494a0c-a99b-43e0-a787-e2e757f7c550', 0, 'e18e3106-5872-4b90-8235-5919033d2408', 0.015000, 'TERAJOULE', 'MEGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('0562e528-168c-423f-949d-70a85f90473b', 0, 'bb67b71c-6a68-48b6-b9af-18fe112a5f25', 0.040200, 'TERAJOULE', 'MEGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('40b956db-b8b0-4fd8-96f4-6a4a8a00a99c', 0, '5f9b341a-264e-452b-96d6-17851b1fed29', 0.009800, 'TERAJOULE', 'MEGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('ebc3c7bd-bdbf-4dcf-b41c-984239514b17', 0, '7d534e3a-30bf-43eb-8c76-294d2cefde38', 0.015600, 'TERAJOULE', 'MEGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('8b3d8927-0817-40e5-a4c4-2b897aa75f74', 0, '123bebca-3b8f-4d29-99b1-184bfef32aab', 0.011800, 'TERAJOULE', 'MEGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('f7161752-baec-4624-aaf5-5f54adecb935', 0, 'f9bae3ee-1ba2-4fbd-9d30-e43c101b6f4e', 0.012500, 'TERAJOULE', 'MEGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('be8d3274-288a-4aa1-bdfd-9933fd2f1f12', 0, 'd73cfb63-1117-4d26-ba59-a240e5184391', 0.029500, 'TERAJOULE', 'MEGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('064bb7e6-749e-4867-9fdb-d611db520715', 0, '20074ddc-e2ce-44be-8e1e-a9070df02f17', 0.027000, 'TERAJOULE', 'MEGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('d190bc76-3cc8-42da-9bc0-0be81566b2ce', 0, '2abe840c-1a44-40b3-b9aa-4e137ab4dc3f', 0.027000, 'TERAJOULE', 'MEGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('86b415be-9928-4d46-a68e-faeb083a5959', 0, '13b9bd26-b35f-49cd-8286-044cb1d89405', 0.027000, 'TERAJOULE', 'MEGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('75ecb506-7997-4d07-95db-238e4bc64773', 0, '02a30f3e-bf8f-4628-aa6f-2958631137ec', 0.019000, 'TERAJOULE',
        'THOUSAND_CUBIC_METER');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('eaafca41-752f-45b4-a596-3452a509460e', 0, 'f8ff0f43-6535-4147-8558-bd274355cf92', 0.019000, 'TERAJOULE',
        'THOUSAND_CUBIC_METER');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('28432ae5-5d6e-4226-803b-6ec9e26a78eb', 0, 'e577f092-a9ad-4057-acc8-63fa869e7878', 0.019000, 'TERAJOULE',
        'THOUSAND_CUBIC_METER');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('61ee1c3a-10b0-475c-ab06-4a06e0c6e3a3', 0, '465fca60-8c15-4762-a37c-0d67a7ab087e', 13600.000000, 'TERAJOULE', 'MEGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('65b65633-569e-4690-bcc8-32252c25b849', 0, '527b7c02-0c6d-4b3a-917d-4a405341d4dc', 0.010000, 'TERAJOULE', 'MEGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('cdea77d1-2f4e-47b2-97a7-67fd7ec5b74e', 0, 'd4ddb9cd-6a35-4134-b114-431fac15e0d0', 0.010500, 'TERAJOULE', 'MEGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('6a0337e2-8184-4dcb-9e3a-29486c19a889', 0, '4ab5ea96-5df4-4386-9e6e-820838e2a875', 0.036000, 'TERAJOULE', 'MEGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('8d96028a-b36b-4b60-8585-644ce6fdb2bb', 0, '96b37ef4-6c8c-4153-9597-96e358cfc7aa', 0.015000, 'TERAJOULE', 'MEGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('e037906f-6180-402f-9830-f8cdac868f03', 0, '95145eb8-7161-4c28-8a6a-56ce61bc53b9', 0.030000, 'TERAJOULE', 'MEGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('262fffa9-e342-40b7-bc77-e511b15473f9', 0, 'd1d39227-1761-472c-8496-2dec73243771', 0.017500, 'TERAJOULE', 'MEGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('dd62b69e-be06-4c93-8d3c-f12a9dd52104', 0, 'd70eb12e-6f33-4c9e-b52e-e1c924c38083', 0.011500, 'TERAJOULE', 'MEGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('b0861b00-9d82-40d6-a519-b952ea671a5c', 0, 'e6b324b9-4f12-4c09-8bcf-acb0eb0cac43', null, null, null);
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('f988e818-28ef-4450-ac5d-953ca8b82675', 0, 'e62b3bda-8fc7-4e14-b817-d088ae305de5', 0.020000, 'TERAJOULE', 'MEGAGRAM');
INSERT INTO energy_conversion (id, version, fuel_id, conversion_value, conversion_unit_numerator, conversion_unit_denominator)
VALUES ('929d19b9-e854-4c64-a3c6-e5445d147d46', 0, '0c3c9895-773c-4147-9a49-aee42854406f', 0.012500, 'TERAJOULE', 'MEGAGRAM');


INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('631c6bb9-9bcf-4eaa-9094-488d3c84759e', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 98.300000, 1.000000, 1.500000, null, 'KILOGRAM', 'TERAJOULE', 'ba9dc6de-926e-4105-93c7-0f4a550fdab7', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, 'ce5c8d55-4010-4168-b90c-808436d9fd32',
        'Hệ số phát thải của than antraxit', 'Emission factor of anthracite coal', '无烟煤的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('0781a448-75cb-4da8-b05d-dcd65d0172f9', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 96.100000, 1.000000, 1.500000, null, 'KILOGRAM', 'TERAJOULE', 'ba9dc6de-926e-4105-93c7-0f4a550fdab7', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, '019868f7-0da4-4771-a8bd-6cb7a99bfc13',
        'Hệ số phát thải của than sub-bitum', 'Emission factor of sub-bituminous coal', '次烟煤的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('8cbc3a63-ed86-4b25-983f-f94cd9d54002', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 73.300000, 3.000000, 0.600000, null, 'KILOGRAM', 'TERAJOULE', 'ba9dc6de-926e-4105-93c7-0f4a550fdab7', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, 'ac6a9b08-010f-41e3-9521-40abbae51084',
        'Hệ số phát thải của dầu thô', 'Emission factor of crude oil', '原油的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('701ea550-14f2-4be9-9a21-7207dd9e5a98', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 74.100000, 3.000000, 0.600000, null, 'KILOGRAM', 'TERAJOULE', 'ba9dc6de-926e-4105-93c7-0f4a550fdab7', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, '7794d70a-7d7d-46f5-aa09-d3902bb360d7',
        'Hệ số phát thải của dầu diesel', 'Emission factor of diesel oil', '柴油的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('e36eaf43-b410-4d78-aeaa-39cc06379f0f', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 74.100000, 3.900000, 3.900000, null, 'KILOGRAM', 'TERAJOULE', '0a0d6d52-ad15-400a-8a43-5e2993757dad', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, '7794d70a-7d7d-46f5-aa09-d3902bb360d7',
        'Hệ số phát thải của dầu diesel (đường bộ)', 'Emission factor of diesel oil (road transport)', '柴油（公路运输）的排放因子',
        false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('d83ca92f-8a80-4dfd-8864-4efb0ec67eb0', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 69.300000, 62.000000, 0.200000, null, 'KILOGRAM', 'TERAJOULE', '0a0d6d52-ad15-400a-8a43-5e2993757dad',
        '', '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, '1161e23b-b7be-402b-8987-024889ab3565',
        'Hệ số phát thải của xăng', 'Emission factor of gasoline', '汽油的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('b646ae22-7528-4090-bb72-cb3039e5dcdb', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 112.000000, 200.000000, 4.000000, null, 'KILOGRAM', 'TERAJOULE', 'ba9dc6de-926e-4105-93c7-0f4a550fdab7',
        '', '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, 'be8d3274-288a-4aa1-bdfd-9933fd2f1f12',
        'Hệ số phát thải của than củi', 'Emission factor of charcoal', '木炭的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('6babd28e-eb49-4bda-9dce-17187ddbbd38', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 70.000000, 0.500000, 2.000000, null, 'KILOGRAM', 'TERAJOULE', 'ca595d21-8b19-4b0c-8d88-4f48c1fe7606', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, '50a6c2d2-603f-4621-bb81-5d07d4dfd7c9',
        'Hệ số phát thải của xăng hàng không', 'Emission factor of aviation gasoline', '航空汽油的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('52dc99cf-9a4d-4bf9-b576-68126ab4140d', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 77.400000, 3.000000, 0.600000, null, 'KILOGRAM', 'TERAJOULE', 'ba9dc6de-926e-4105-93c7-0f4a550fdab7', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, 'ab8a8af6-56e2-49f8-8e96-c0d2aae90aad',
        'Hệ số phát thải của dầu nhiên liệu', 'Emission factor of fuel oil', '燃油的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('789ed922-81f2-4e12-a77b-9618e332609e', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 56.100000, 1.000000, 0.100000, null, 'KILOGRAM', 'TERAJOULE', 'ba9dc6de-926e-4105-93c7-0f4a550fdab7', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, 'a940ef6c-3b1d-4f5f-bc50-ca32f82a4faa',
        'Hệ số phát thải của khí tự nhiên', 'Emission factor of natural gas', '天然气的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('9e9f5e27-728a-436e-89f4-0ad306dc10c6', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 100.000000, 30.000000, 4.000000, null, 'KILOGRAM', 'TERAJOULE', 'ba9dc6de-926e-4105-93c7-0f4a550fdab7',
        '', '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, '61ee1c3a-10b0-475c-ab06-4a06e0c6e3a3',
        'Hệ số phát thải của sinh khối', 'Emission factor of biomass', '生物质的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('5d9c920d-fcb2-4424-bc91-f8bc8d9c33ba', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 98.300000, 10.000000, 1.500000, null, 'KILOGRAM', 'TERAJOULE', 'ac6d6897-5702-425d-8c24-e83b8f7dbd34',
        '', '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, 'ce5c8d55-4010-4168-b90c-808436d9fd32',
        'Hệ số phát thải của than antraxit', 'Emission factor of anthracite coal', '无烟煤的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('014be772-8bd7-4631-ab20-4ce81da6d83d', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 74.100000, 3.000000, 0.600000, null, 'KILOGRAM', 'TERAJOULE', 'ac6d6897-5702-425d-8c24-e83b8f7dbd34', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, '7794d70a-7d7d-46f5-aa09-d3902bb360d7',
        'Hệ số phát thải của dầu diesel', 'Emission factor of diesel oil', '柴油的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('28891f2b-c6e6-4ad1-a4bf-b7f734f4a91f', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 77.400000, 3.000000, 0.600000, null, 'KILOGRAM', 'TERAJOULE', 'ac6d6897-5702-425d-8c24-e83b8f7dbd34', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, 'ab8a8af6-56e2-49f8-8e96-c0d2aae90aad',
        'Hệ số phát thải của dầu nhiên liệu', 'Emission factor of fuel oil', '燃油的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('df9b69ce-8a6b-46b9-ab26-a39d0be0212a', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 56.100000, 1.000000, 0.100000, null, 'KILOGRAM', 'TERAJOULE', 'ac6d6897-5702-425d-8c24-e83b8f7dbd34', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, 'a940ef6c-3b1d-4f5f-bc50-ca32f82a4faa',
        'Hệ số phát thải của khí tự nhiên', 'Emission factor of natural gas', '天然气的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('f0da6515-4990-4e7f-8342-f65ecdc0b067', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 0.000000, 30.000000, 4.000000, null, 'KILOGRAM', 'TERAJOULE', 'ac6d6897-5702-425d-8c24-e83b8f7dbd34', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, '61ee1c3a-10b0-475c-ab06-4a06e0c6e3a3',
        'Hệ số phát thải của sinh khối', 'Emission factor of biomass', '生物质的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('cd4887c3-6b28-4148-ab48-7e4af7c25bdb', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 71.500000, 0.500000, 2.000000, null, 'KILOGRAM', 'TERAJOULE', 'ca595d21-8b19-4b0c-8d88-4f48c1fe7606', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, '78227fc9-2191-4dd9-8029-1b266416bee9',
        'Hệ số phát thải của nhiên liệu hàng không', 'Emission factor of aviation fuel (Jet Kerosene)',
        '航空燃料（喷气煤油）的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('e33e3497-f937-414b-955a-fcc2fa795793', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 74.100000, 3.900000, 3.900000, null, 'KILOGRAM', 'TERAJOULE', '0a0d6d52-ad15-400a-8a43-5e2993757dad', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, '7794d70a-7d7d-46f5-aa09-d3902bb360d7',
        'Hệ số phát thải của dầu diesel', 'Emission factor of diesel oil', '柴油的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('e1511b6b-c305-408e-8f44-76782aa0d71c', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 56.100000, 92.000000, 3.000000, null, 'KILOGRAM', 'TERAJOULE', '0a0d6d52-ad15-400a-8a43-5e2993757dad',
        '', '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, 'a940ef6c-3b1d-4f5f-bc50-ca32f82a4faa',
        'Hệ số phát thải của khí tự nhiên', 'Emission factor of natural gas', '天然气的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('c45c05b3-45b6-4fa2-904e-0a9da77a94c3', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 74.100000, 4.150000, 28.600000, null, 'KILOGRAM', 'TERAJOULE', '81264f0b-87ea-4ad3-9661-442e93c48041',
        '', '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, '7794d70a-7d7d-46f5-aa09-d3902bb360d7',
        'Hệ số phát thải của dầu diesel', 'Emission factor of diesel oil', '柴油的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('140ebeb1-1a68-4a3e-bcdd-576ca42c34e6', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 74.100000, 7.000000, 2.000000, null, 'KILOGRAM', 'TERAJOULE', '1161d039-0831-4375-b9a4-24695ecae65b', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, '7794d70a-7d7d-46f5-aa09-d3902bb360d7',
        'Hệ số phát thải của dầu diesel', 'Emission factor of diesel oil', '柴油的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('1d9787df-ad83-4c3d-a798-b302d65e1d6c', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 77.400000, 7.000000, 2.000000, null, 'KILOGRAM', 'TERAJOULE', '1161d039-0831-4375-b9a4-24695ecae65b', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, 'ab8a8af6-56e2-49f8-8e96-c0d2aae90aad',
        'Hệ số phát thải của dầu nhiên liệu', 'Emission factor of fuel oil', '燃油的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('000b8b3e-0337-420e-9b6d-b62c09c336a4', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 98.300000, 10.000000, 1.500000, null, 'KILOGRAM', 'TERAJOULE', 'e1884bed-7223-48a7-9568-b53d0b133727',
        '', '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, 'ce5c8d55-4010-4168-b90c-808436d9fd32',
        'Hệ số phát thải của than antraxit', 'Emission factor of anthracite coal', '无烟煤的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('bd8375e7-3719-41c9-bc2d-c9adf43dcd4a', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 74.100000, 10.000000, 0.600000, null, 'KILOGRAM', 'TERAJOULE', 'e1884bed-7223-48a7-9568-b53d0b133727',
        '', '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, '7794d70a-7d7d-46f5-aa09-d3902bb360d7',
        'Hệ số phát thải của dầu diesel', 'Emission factor of diesel oil', '柴油的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('9ae8bd54-aed7-45ad-9028-5f3e464b0b19', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 112.000000, 200.000000, 1.000000, null, 'KILOGRAM', 'TERAJOULE', 'e1884bed-7223-48a7-9568-b53d0b133727',
        '', '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, 'be8d3274-288a-4aa1-bdfd-9933fd2f1f12',
        'Hệ số phát thải của than củi', 'Emission factor of charcoal', '木炭的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('7dbc7e42-cc44-467a-87f6-7e49b7898375', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 98.300000, 300.000000, 1.500000, null, 'KILOGRAM', 'TERAJOULE', 'a0151298-d6cf-493a-ad1b-8ab1db8de881',
        '', '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, 'ce5c8d55-4010-4168-b90c-808436d9fd32',
        'Hệ số phát thải của than antraxit', 'Emission factor of anthracite coal', '无烟煤的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('478e64c3-61dc-4b93-89c3-ead08f6fb819', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 71.900000, 10.000000, 0.600000, null, 'KILOGRAM', 'TERAJOULE', 'a0151298-d6cf-493a-ad1b-8ab1db8de881',
        '', '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, 'c99109bf-baee-46db-a49a-90293237a046',
        'Hệ số phát thải của dầu hoả', 'Emission factor of kerosene', '煤油的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('97c6bba5-50ee-4813-87b2-a382eee69713', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 100.000000, 300.000000, 4.000000, null, 'KILOGRAM', 'TERAJOULE', 'a0151298-d6cf-493a-ad1b-8ab1db8de881',
        '', '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, '65b65633-569e-4690-bcc8-32252c25b849',
        'Hệ số phát thải của sinh khối', 'Emission factor of biomass', '生物质的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('d9f13e96-e644-4842-a53b-418208d4d654', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 69.300000, 10.000000, 0.600000, null, 'KILOGRAM', 'TERAJOULE', 'c1dae190-34c1-4462-91e2-2f1fa5b0a782',
        '', '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, '1161e23b-b7be-402b-8987-024889ab3565',
        'Hệ số phát thải của xăng', 'Emission factor of gasoline', '汽油的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('01f728d0-3be7-4561-8e4c-dcb64bbf06df', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 74.100000, 10.000000, 0.600000, null, 'KILOGRAM', 'TERAJOULE', 'c1dae190-34c1-4462-91e2-2f1fa5b0a782',
        '', '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, '7794d70a-7d7d-46f5-aa09-d3902bb360d7',
        'Hệ số phát thải của dầu diesel', 'Emission factor of diesel oil', '柴油的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('d0ebb141-d950-4cc4-9820-eabdcf9ab819', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 0.000000, 300.000000, 4.000000, null, 'KILOGRAM', 'TERAJOULE', 'c1dae190-34c1-4462-91e2-2f1fa5b0a782',
        '', '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, '61ee1c3a-10b0-475c-ab06-4a06e0c6e3a3',
        'Hệ số phát thải của sinh khối', 'Emission factor of biomass', '生物质的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('1f8efa68-0928-455f-97ac-ab415f3e180b', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 0.000000, 16.000000, 0.000000, null, 'CUBIC_METER', 'MEGAGRAM', '0de5518d-8f3e-4975-be35-21dda2b15925',
        '', '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null, 'Hệ số phát tán trong khai thác than hầm lò',
        'Emission factor in underground coal mining', '地下煤矿开采中的甲烷排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('541e1637-e941-43e9-9ce3-3cdb4a185805', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 0.000000, 0.169700, 0.000000, null, 'CUBIC_METER', 'MEGAGRAM', '0de5518d-8f3e-4975-be35-21dda2b15925',
        '', '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null, 'Hệ số phát tán sau khai thác than hầm lò',
        'Emission factor after underground coal mining', '地下煤矿开采后甲烷排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('4193fdcc-4ceb-49d9-8d04-888e76c3794b', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 0.000000, 0.053750, 0.000000, null, 'CUBIC_METER', 'MEGAGRAM', '16563e66-baf8-4fb5-8719-2b4cecfd94a8',
        '', '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Hệ số phát tán trong khai thác than lộ thiên', 'Emission factor in surface coal mining', '露天煤矿开采中的甲烷排放因子',
        false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('43fb51d4-9153-4dfc-abfe-c7a989300fdd', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 0.000000, 0.169700, 0.000000, null, 'CUBIC_METER', 'MEGAGRAM', '16563e66-baf8-4fb5-8719-2b4cecfd94a8',
        '', '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null, 'Hệ số phát tán sau khai thác than lộ thiên',
        'Emission factor after surface coal mining', '露天煤矿开采后甲烷排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('6e45e9ac-036f-4ffc-b042-2aa876e55c11', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 0.002150, 0.010350, 0.000000, null, 'GIGAGRAM', 'THOUSAND_CUBIC_METER',
        '779af8bc-6b7c-4845-8a9c-f6c608d0be47', '', '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Hệ số phát thải rò rỉ từ sản xuất dầu', 'Emission factor of leakage from oil production', '石油生产泄漏排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('8b9178e4-86bf-4f3b-b9a6-00178f5eb4c7', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 0.040500, 0.000025, 0.000001, null, 'GIGAGRAM', 'THOUSAND_CUBIC_METER',
        '779af8bc-6b7c-4845-8a9c-f6c608d0be47', '', '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Hệ số phát thải do đốt cháy tự nhiên từ sản xuất dầu', 'Emission factor of natural combustion from oil production',
        '石油生产自然燃烧排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('a3bfdedc-d89b-4e8c-b3b1-3b542ec39bf1', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 0.002490, 0.019600, 0.000000, null, 'GIGAGRAM', 'THOUSAND_CUBIC_METER',
        '779af8bc-6b7c-4845-8a9c-f6c608d0be47', '', '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Hệ số phát thải phát tán trong sản xuất dầu', 'Emission factor of fugitive emissions in oil production',
        '石油生产中的无组织排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('3b178e71-7443-49be-b87e-8dc55accaae1', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 0.067500, 0.000000, 0.000000, null, 'GIGAGRAM', 'MILLION_CUBIC_METER',
        '75c1d81d-8b4f-4fac-b3a1-ffbaa3f7ea7f', '', '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Hệ số phát thải rò rỉ trong xử lý khí', 'Emission factor of leakage in gas processing', '天然气处理泄漏排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('5e032fc5-bb60-45cc-8cee-131233a83b3c', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 0.003550, 0.000000, 0.000000, null, 'GIGAGRAM', 'MILLION_CUBIC_METER',
        '75c1d81d-8b4f-4fac-b3a1-ffbaa3f7ea7f', '', '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Hệ số phát thải do đốt cháy tự nhiên từ sản xuất dầu', 'Emission factor of natural combustion from oil production',
        '石油生产自然燃烧排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('c2ac41b0-563d-477a-a81b-07748a3cd790', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 0.001400, 0.000001, 0.000000, null, 'GIGAGRAM', 'MILLION_CUBIC_METER',
        '75c1d81d-8b4f-4fac-b3a1-ffbaa3f7ea7f', '', '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Hệ số phát thải do đốt cháy tự nhiên từ sản xuất khí', 'Emission factor of natural combustion from gas production',
        '天然气生产自然燃烧排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('ea9f1c8b-4ff4-4db2-8b9c-d8714c08bcc1', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 0.000097, 0.012190, 0.000000, null, 'GIGAGRAM', 'MILLION_CUBIC_METER',
        '75c1d81d-8b4f-4fac-b3a1-ffbaa3f7ea7f', '', '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Hệ số phát thải phát tán trong sản xuất khí', 'Emission factor of fugitive emissions in gas production',
        '天然气生产中的无组织排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('5e8d4c0d-3206-4800-a112-fc54ec91b540', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 0.000250, 0.000790, 0.000000, null, 'GIGAGRAM', 'MILLION_CUBIC_METER',
        '75c1d81d-8b4f-4fac-b3a1-ffbaa3f7ea7f', '', '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Hệ số phát thải phát tán trong xử lý khí', 'Emission factor of fugitive emissions in gas processing',
        '天然气处理中的无组织排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('076dbf1e-c287-40e4-867d-3b9298f6a03c', 0, 'seed_data_sql', '2025-05-28 19:17:50.291546', '2025-05-28 19:17:50.291546',
        'seed_data_sql', 0.692500, 0.000000, 0.000000, null, 'KILOGRAM', 'KWH', '62f3128e-e2dc-4cdf-ac07-b072f9908c30', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null, 'Phát thải lưới điện Việt Nam',
        'Emissions from the Vietnam Power Grid', '越南电网排放', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('18062444-f944-444a-822e-11da2ee00d73', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 0.800000, 0.000000, null, null, null, 'b49cff33-2eb2-4bde-88ca-906597294c6a', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Hệ số điều chỉnh mê-tan (MCF) của bãi chôn lấp không quản lý-sâu ( độ sâu từ 5m trở lên)',
        'Methane Correction Factor (MCF) of Deep Unmanaged Landfills (5m or more in depth)',
        '深层无人管理垃圾填埋场（深度 5 米或以上）的甲烷校正系数 (MCF)', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('2b44185b-707b-4c28-bcf6-a0a7074d5015', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 0.400000, 0.000000, null, null, null, 'b49cff33-2eb2-4bde-88ca-906597294c6a', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Hệ số điều chỉnh mê-tan (MCF) của bãi chôn lấp không quản lý-nông ( độ sâu dưới 5m )',
        'Methane Correction Factor (MCF) of Unmanaged Landfills-Shallow (depth less than 5m)',
        '无人管理的浅层垃圾填埋场（深度小于 5 米）的甲烷校正系数 (MCF)', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('136b0a0e-a34f-40b2-b93c-8fd4739061ae', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 63.100000, 5.000000, 0.100000, null, 'KILOGRAM', 'TERAJOULE', 'a0151298-d6cf-493a-ad1b-8ab1db8de881', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, 'bd30dfa7-5f82-45ba-a29c-342ce02efd5c',
        'Hệ số phát thải của khí hóa lỏng', 'Emission factor of blast furnace gas', '高炉煤气的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('e302ba83-c787-4ff2-bd2e-84433b01313d', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 1.000000, 0.000000, null, null, null, 'b49cff33-2eb2-4bde-88ca-906597294c6a', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Hệ số điều chỉnh mê-tan (MCF) của bãi chôn lấp quản lý- kỵ khí',
        'Methane Correction Factor (MCF) of Managed-Anaerobic Landfills', '管理型厌氧垃圾填埋场的甲烷校正系数 (MCF)', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('6ce3b651-a3f9-47ed-961f-aa982c19131e', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 0.500000, 0.000000, null, null, null, 'b49cff33-2eb2-4bde-88ca-906597294c6a', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Hệ số điều chỉnh mê-tan (MCF) của bãi chôn lấp quản lý tốt - bán hiếu khí',
        'Methane Correction Factor (MCF) of Well-Managed Landfills - Semi-Aerobic',
        '管理良好的垃圾填埋场的甲烷校正系数 (MCF) - 半需氧', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('8e7e2420-3a0e-4ca8-b9fb-4fff50ea8a62', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 0.700000, 0.000000, null, null, null, 'b49cff33-2eb2-4bde-88ca-906597294c6a', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Hệ số điều chỉnh mê-tan (MCF) của bãi chôn lấp quản lý kém - bán hiếu khí',
        'Methane Correction Factor (MCF) of Poorly Managed - Semi-Aerobic Landfills',
        '管理不善的半需氧垃圾填埋场的甲烷校正系数 (MCF)', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('fb0eb1fd-d4ab-49dd-bdbd-580c806547cb', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 0.400000, 0.000000, null, null, null, 'b49cff33-2eb2-4bde-88ca-906597294c6a', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Hệ số điều chỉnh mê-tan (MCF) của bãi chôn lấp quản lý tốt - Sục khí liên tục',
        'Methane Correction Factor (MCF) of a Well-Managed Landfill - Continuous Aeration',
        '管理良好的垃圾填埋场的甲烷校正系数 (MCF) - 连续曝气', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('12c10977-3608-49d3-8df3-4b24fc87063f', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 0.700000, 0.000000, null, null, null, 'b49cff33-2eb2-4bde-88ca-906597294c6a', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Hệ số điều chỉnh mê-tan (MCF) của bãi chôn lấp quản lý kém - Sục khí liên tục',
        'Methane Correction Factor (MCF) for Poorly Managed Landfills - Continuous Aeration',
        '管理不善的垃圾填埋场的甲烷校正系数 (MCF) - 连续曝气', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('fa1041a9-6c6a-43bb-8de0-12bc77d6fe96', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 0.600000, 0.000000, null, null, null, 'b49cff33-2eb2-4bde-88ca-906597294c6a', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Hệ số điều chỉnh mê-tan (MCF) của bãi chôn lấp còn lại', 'Methane Correction Factor (MCF) of the Residual Landfill',
        '剩余垃圾填埋场的甲烷校正系数 (MCF)', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('face9de6-da8c-4906-879a-97b0815b26e0', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 15.000000, 0.000000, null, 'PERCENT', 'KILOGRAM', 'b49cff33-2eb2-4bde-88ca-906597294c6a', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Carbon hữu cơ phân huỷ (DOC) của thức ăn. chất hữu cơ', 'Degradable organic carbon (DOC) of food. organic matter',
        '食物的可降解有机碳（DOC）。有机质', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('ec0524f3-ce32-403b-8a37-3f87107d1ebb', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 20.000000, 0.000000, null, 'PERCENT', 'KILOGRAM', 'b49cff33-2eb2-4bde-88ca-906597294c6a', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null, 'Carbon hữu cơ phân huỷ (DOC) của cây cối',
        'Decomposable organic carbon (DOC) of plants', '植物的可分解有机碳（DOC）', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('f517d389-8b6a-4783-b2be-1a1e462c01bd', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 40.000000, 0.000000, null, 'PERCENT', 'KILOGRAM', 'b49cff33-2eb2-4bde-88ca-906597294c6a', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null, 'Carbon hữu cơ phân huỷ (DOC) của giấy',
        'Degradable organic carbon (DOC) of paper', '纸张的可降解有机碳（DOC）', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('e2c07787-661f-4d0c-ab12-b0f7436d95a8', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 43.000000, 0.000000, null, 'PERCENT', 'KILOGRAM', 'b49cff33-2eb2-4bde-88ca-906597294c6a', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null, 'Carbon hữu cơ phân huỷ (DOC) của gỗ',
        'Degradable organic carbon (DOC) of wood', '木材的可降解有机碳（DOC）', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('bb4b750d-d0ea-4aff-a244-1033c0525c17', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 24.000000, 0.000000, null, 'PERCENT', 'KILOGRAM', 'b49cff33-2eb2-4bde-88ca-906597294c6a', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null, 'Carbon hữu cơ phân huỷ (DOC) của dệt may',
        'Degradable organic carbon (DOC) of textiles', '纺织品的可降解有机碳 (DOC)', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('a2f6fb36-bc4e-4b8c-bba9-72df07441935', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 24.000000, 0.000000, null, 'PERCENT', 'KILOGRAM', 'b49cff33-2eb2-4bde-88ca-906597294c6a', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null, 'Carbon hữu cơ phân huỷ (DOC) của tã lót',
        'Degradable organic carbon (DOC) of diapers', '尿布的可生物降解有机碳 (DOC)', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('dcb8b2da-3cb8-4024-a35f-091e4c45fd7e', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 5.000000, 0.000000, null, 'PERCENT', 'KILOGRAM', 'b49cff33-2eb2-4bde-88ca-906597294c6a', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null, 'Carbon hữu cơ phân huỷ (DOC) của bùn thải',
        'Degradable organic carbon (DOC) of sludge', '污泥的可降解有机碳（DOC）', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('07d931da-21bc-4d35-bc51-1e1dc967446a', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 15.000000, 0.000000, null, 'PERCENT', 'KILOGRAM', 'b49cff33-2eb2-4bde-88ca-906597294c6a', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Carbon hữu cơ phân huỷ (DOC) của chát thải công nghiệp', 'Degradable organic carbon (DOC) of industrial waste',
        '工业废弃物的可降解有机碳（DOC）', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('2c194cdf-09aa-4815-9deb-b44ce4014416', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 10.000000, 0.000000, null, 'PERCENT', 'KILOGRAM', 'b49cff33-2eb2-4bde-88ca-906597294c6a', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Tỷ lệ DOC dễ phân huỷ trong điều kiện kỵ khí (DOCf) - Chất thải khó phân huỷ: gỗ. sản phẩm chế tạo từ gỗ. cành cây.…',
        'Anaerobic biodegradable DOC fraction (DOCf) - Hardly biodegradable waste: wood. wood products. tree branches.…',
        '厌氧可生物降解DOC部分（DOCf）- 难以生物降解的废物：木材。木制品树枝。…', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('7e0662ab-8256-4ec0-8e24-7cf5c5f74beb', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 50.000000, 0.000000, null, 'PERCENT', 'KILOGRAM', 'b49cff33-2eb2-4bde-88ca-906597294c6a', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Tỷ lệ DOC dễ phân huỷ trong điều kiện kỵ khí (DOCf) - Chất thải phân huỷ trung bình : giấy. các sản phẩm dệt may. tã lót.',
        'Anaerobic biodegradable DOC fraction (DOCf) - Medium biodegradable waste: paper. textile products. diapers.',
        '厌氧可生物降解DOC组分（DOCf）-中等可生物降解废物：纸张。纺织产品尿布。', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('5990e75d-475d-43e7-9e9e-e0e83ee8b86f', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 70.000000, 0.000000, null, 'PERCENT', 'KILOGRAM', 'b49cff33-2eb2-4bde-88ca-906597294c6a', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Tỷ lệ DOC dễ phân huỷ trong điều kiện kỵ khí (DOCf) - Chất thải dễ phân huỷ  : thức ăn thừa. cỏ ( rác vườn trừ cây cảnh)',
        'Anaerobic biodegradable DOC ratio (DOCf) - Biodegradable waste: food waste, grass (garden waste except ornamental plants)',
        '厌氧可生物降解DOC部分（DOCf）-可生物降解的废物：食物垃圾。草（除观赏植物外的园林废弃物）', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('ba614a00-e729-44e6-96b2-e6be33938db4', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 50.000000, 0.000000, null, 'PERCENT', 'KILOGRAM', 'b49cff33-2eb2-4bde-88ca-906597294c6a', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null, 'Tỷ lệ lượng CH₄ trong khí từ bãi rác',
        'CH₄ content in landfill gas', '垃圾填埋气中CH₄含量', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('f0c08aee-0bf4-490a-ab11-0fbba6d20b24', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.600000, 10.000000, 0.000000, null, 'GRAM', 'KILOGRAM', '23b5abbb-f546-4024-8674-f03c318f0500', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Xử lý chất thải bằng phương pháp ủ phân sinh học (composting) - Hệ số phát thải theo trọng lượng khô',
        'Composting Waste Treatment - Emission Factor on a Dry Weight Basis', '堆肥废物处理 - 干重排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('bb9d135b-2855-4209-b061-9fe2457af179', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.240000, 4.000000, 0.000000, null, 'GRAM', 'KILOGRAM', '23b5abbb-f546-4024-8674-f03c318f0500', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Xử lý chất thải bằng phương pháp ủ phân sinh học (composting) - Hệ số phát thải theo trọng lượng ướt',
        'Composting Waste Treatment - Emission Factor on Wet Weight Basis', '堆肥废物处理 - 湿重排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('eaba79d1-d953-4642-a448-7f33240dbe5f', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 2.000000, 0.000000, null, 'GRAM', 'KILOGRAM', '23b5abbb-f546-4024-8674-f03c318f0500', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Xử lý bằng hầm Biogas kỵ khí- Hệ số phát thải theo trọng lượng khô',
        'Anaerobic Biogas Treatment - Emission Factor by Dry Weight', '厌氧沼气处理 - 干重排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('b19f0d89-4de1-4218-8d5b-f2308de6ec72', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 0.800000, 0.000000, null, 'GRAM', 'KILOGRAM', '23b5abbb-f546-4024-8674-f03c318f0500', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Xử lý bằng hầm Biogas kỵ khí- Hệ số phát thải theo trọng lượng ướt',
        'Anaerobic Biogas Treatment - Emission Factor by Wet Weight', '厌氧沼气处理 - 湿重排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('34257232-b884-4917-bdc8-e43cef4c78c1', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 100.000000, 0.000000, 0.000000, null, 'PERCENT', null, 'eb6614e7-ff4b-478c-b9ee-061b6cf6258b', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Hàm lượng khô theo thành phần chất thải rắn của nhựa', 'Dry content according to solid waste composition of plastic',
        '根据固体废物中塑料的成分计算干含量', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('a5b35cb0-0744-4adc-bf00-d25b147ac150', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 80.000000, 0.000000, 0.000000, null, 'PERCENT', null, 'eb6614e7-ff4b-478c-b9ee-061b6cf6258b', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Hàm lượng khô theo thành phần chất thải rắn của dệt may',
        'Dry content according to the composition of textile solid waste', '根据纺织固体废弃物的成分计算的干含量', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('1478d689-d6f4-44d0-8211-e0a27e015aec', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 85.000000, 0.000000, 0.000000, null, 'PERCENT', null, 'eb6614e7-ff4b-478c-b9ee-061b6cf6258b', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Hàm lượng khô theo thành phần chất thải rắn của gỗ', 'Dry content according to solid waste composition of wood',
        '根据木材固体废物成分计算的干物质含量', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('54eaa385-6a3e-4728-82ca-c43d53b11bd9', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 40.000000, 0.000000, 0.000000, null, 'PERCENT', null, 'eb6614e7-ff4b-478c-b9ee-061b6cf6258b', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Hàm lượng khô theo thành phần chất thải rắn của tã lót', 'Dry content according to solid waste composition of diapers',
        '根据尿布固体废物成分计算的干物质含量', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('47730460-7cef-4378-b639-99a25a18e11b', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 40.000000, 0.000000, 0.000000, null, 'PERCENT', null, 'eb6614e7-ff4b-478c-b9ee-061b6cf6258b', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Hàm lượng khô theo thành phần chất thải rắn của thực phẩm. chất hữu cơ',
        'Dry content according to solid waste composition of food. organic matter', '根据食物固体废物的成分计算的干含量。有机质',
        false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('f54597e7-814f-4943-92d2-34e9e418274e', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 40.000000, 0.000000, 0.000000, null, 'PERCENT', null, 'eb6614e7-ff4b-478c-b9ee-061b6cf6258b', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Hàm lượng khô theo thành phần chất thải rắn của cây cối', 'Dry content according to solid waste composition of plants',
        '根据工厂固体废物成分计算的干物质含量', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('35df61a8-4e07-4919-9ccc-2d13be0c40ba', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 90.000000, 0.000000, 0.000000, null, 'PERCENT', null, 'eb6614e7-ff4b-478c-b9ee-061b6cf6258b', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Hàm lượng khô theo thành phần chất thải rắn của giấy', 'Dry content according to solid waste composition of paper',
        '根据纸张固体废物成分计算的干物质含量', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('8593625e-fbae-4360-840f-509accdbd234', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 75.000000, 0.000000, 0.000000, null, 'PERCENT', null, 'eb6614e7-ff4b-478c-b9ee-061b6cf6258b', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null, 'Tỷ lệ hàm lượng carbon của nhựa ( CF)',
        'Carbon content ratio of plastic ( CF)', '塑料含碳量比（CF）', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('d4fc39bc-d62e-4f24-8a54-73d682007812', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 50.000000, 0.000000, 0.000000, null, 'PERCENT', null, 'eb6614e7-ff4b-478c-b9ee-061b6cf6258b', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null, 'Tỷ lệ hàm lượng carbon của dệt may ( CF)',
        'Carbon content of textiles (CF)', '纺织品的碳含量（CF）', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('431bd4ee-6026-42fe-8c7f-bad02f42f69b', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 50.000000, 0.000000, 0.000000, null, 'PERCENT', null, 'eb6614e7-ff4b-478c-b9ee-061b6cf6258b', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null, 'Tỷ lệ hàm lượng carbon của gỗ ( CF)',
        'Carbon content of wood (CF)', '木材碳含量（CF）', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('baaa943a-6e9b-4596-ae1c-b02a43031f89', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 70.000000, 0.000000, 0.000000, null, 'PERCENT', null, 'eb6614e7-ff4b-478c-b9ee-061b6cf6258b', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null, 'Tỷ lệ hàm lượng carbon của tã lót ( CF)',
        'Carbon content ratio of diapers ( CF)', '纸尿裤含碳量比（CF）', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('451e6653-2546-4a93-817e-a42a642a9ab1', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 38.000000, 0.000000, 0.000000, null, 'PERCENT', null, 'eb6614e7-ff4b-478c-b9ee-061b6cf6258b', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Tỷ lệ hàm lượng carbon của thực phẩm. chất hữu cơ ( CF)', 'The carbon content of food. organic matter ( CF)',
        '食物中碳含量的百分比。有机物（CF）', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('46fefadf-b459-44aa-a964-3a5129fdce43', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 49.000000, 0.000000, 0.000000, null, 'PERCENT', null, 'eb6614e7-ff4b-478c-b9ee-061b6cf6258b', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null, 'Tỷ lệ hàm lượng carbon của cây cối ( CF)',
        'Carbon content of trees ( CF)', '树木的碳含量（CF）', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('c587c002-34d4-4092-b76e-eabe8b7fc73e', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 46.000000, 0.000000, 0.000000, null, 'PERCENT', null, 'eb6614e7-ff4b-478c-b9ee-061b6cf6258b', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null, 'Tỷ lệ hàm lượng carbon của giấy ( CF)',
        'Carbon content of paper ( CF)', '纸张含碳量（CF）', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('9f2206f6-635d-4d6e-ba0f-43740e0110ba', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 100.000000, 0.000000, 0.000000, null, 'PERCENT', null, 'eb6614e7-ff4b-478c-b9ee-061b6cf6258b', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Tỷ lệ carbon hoá thạch trong tổng số carbon của nhựa ( FCF)', 'Fossil carbon fraction of total carbon of plastic (FCF)',
        '化石碳占塑料碳总量 (FCF) 的比例', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('954a1dcd-4afe-41c1-ac64-498b21722fd6', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 20.000000, 0.000000, 0.000000, null, 'PERCENT', null, 'eb6614e7-ff4b-478c-b9ee-061b6cf6258b', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Tỷ lệ carbon hoá thạch trong tổng số carbon của dệt may ( FCF)', 'Fossil carbon share of total textile carbon (FCF)',
        '化石碳占纺织品碳排放总量的份额（FCF）', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('669d1ffc-5860-4964-a051-2a3ac2773bbd', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 10.000000, 0.000000, 0.000000, null, 'PERCENT', null, 'eb6614e7-ff4b-478c-b9ee-061b6cf6258b', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Tỷ lệ carbon hoá thạch trong tổng số carbon của tã lót ( FCF)',
        'Fossil carbon fraction of total carbon of diapers (FCF)', '尿布总碳含量的化石碳分数（FCF）', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('5e0690bc-56a8-4656-8407-f927262b7d18', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 0.000000, 0.000000, null, 'PERCENT', null, 'eb6614e7-ff4b-478c-b9ee-061b6cf6258b', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Tỷ lệ carbon hoá thạch trong tổng số carbon của cây cối ( FCF)', 'Fossil carbon fraction of total tree carbon (FCF)',
        '化石碳占树木总碳含量 (FCF)', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('1c54adff-2f9a-402f-8716-6e0e9eb022d3', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 1.000000, 0.000000, 0.000000, null, 'PERCENT', null, 'eb6614e7-ff4b-478c-b9ee-061b6cf6258b', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Tỷ lệ carbon hoá thạch trong tổng số carbon của giấy ( FCF)', 'Fossil carbon fraction of total paper carbon (FCF)',
        '化石碳占纸张碳总量的比例（FCF）', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('5d38f860-5b02-4abb-a50c-3a5b627edda8', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 100.000000, 0.000000, 0.000000, null, 'PERCENT', null, '86fe5fc5-1225-42f8-ac53-1f1addd2cfc8', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null, 'Hệ số oxy hoá OF', 'OF oxidation coefficient',
        'OF氧化系数', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('acf96dab-d1a8-4223-b696-9eaf13df8ddb', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 0.000200, 47.000000, null, 'KILOGRAM', 'TERAGRAM', '86fe5fc5-1225-42f8-ac53-1f1addd2cfc8', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null, 'Hệ số phát thải của Lò đốt vỉ động liên tục',
        'Emission factor of Continuous Moving Grate Incinerator', '连续移动炉排焚烧炉的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('328f5410-3c04-462d-930c-7614201ea8bb', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 0.000000, 67.000000, null, 'KILOGRAM', 'TERAGRAM', '86fe5fc5-1225-42f8-ac53-1f1addd2cfc8', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null, 'Hệ số phát thải của Lò đốt tầng sôi liên tục',
        'Emission factor of Continuous Fluidized Bed Incinerator', '连续流化床焚烧炉的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('fcb6b056-f672-4dab-936d-9370771938b9', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 0.006000, 41.000000, null, 'KILOGRAM', 'TERAGRAM', '86fe5fc5-1225-42f8-ac53-1f1addd2cfc8', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null, 'Hệ số phát thải của Lò đốt vỉ động bán liên tục',
        'Emission factor of Semi-Continuous Moving Grate Incinerator', '半连续移动炉排焚烧炉的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('13c927d3-033c-4b7d-8b3e-ff33bfaae3c9', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 0.188000, 68.000000, null, 'KILOGRAM', 'TERAGRAM', '86fe5fc5-1225-42f8-ac53-1f1addd2cfc8', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Hệ số phát thải của Lò đốt tầng sôi bán liên tục', 'Emission factor of Semi-Continuous Fluidized Bed Incinerator',
        '半连续流化床焚烧炉的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('4553bc78-deb2-4552-a5ee-24d94f0de610', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 0.060000, 56.000000, null, 'KILOGRAM', 'TERAGRAM', '86fe5fc5-1225-42f8-ac53-1f1addd2cfc8', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Hệ số phát thải của Lò đốt vỉ động - đốt hàng loạt', 'Emission factor of Batch Moving Grate Incinerator',
        '间歇式移动炉排焚烧炉的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('df655494-a4c5-49c5-9b29-002bc7b0f3d8', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 0.237000, 221.000000, null, 'KILOGRAM', 'TERAGRAM', '86fe5fc5-1225-42f8-ac53-1f1addd2cfc8', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Hệ số phát thải của Lò đốt tầng sôi - đốt hàng loạt', 'Emission factor of Batch Fluidized Bed Incinerator',
        '间歇式流化床焚烧炉的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('269a7e55-7a30-4117-b332-b5a24f124b44', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 71.000000, 0.000000, 0.000000, null, 'PERCENT', null, '31dc71b8-c19e-4363-8df0-d0f75430f21b', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null, 'Hệ số oxy hoá OF', 'OF oxidation coefficient',
        'OF氧化系数', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('9e0151b6-fdc9-4085-b155-a3d4dd16e2aa', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 6.500000, 150.000000, null, 'GRAM', 'MEGAGRAM', '31dc71b8-c19e-4363-8df0-d0f75430f21b', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null, 'Hệ số phát thải của Đốt lộ thiên chất thải',
        'Emission factor of Open Burning of Waste', '露天焚烧废弃物的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('0324656e-24bb-4ecd-8134-9bcc98d538f4', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 0.600000, 0.000000, null, null, null, '7776a811-2367-4d58-a8d2-cc7b5ab878c2', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null, 'Khả năng phát sinh khí CH₄ tối đa - B0',
        'Maximum CH₄ gas generation potential - B0', '最大 CH₄ 气体生成潜力 - B0', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('cbe82bdd-8187-474e-908b-afbba36c6b2c', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 0.300000, 0.000000, null, null, null, '7776a811-2367-4d58-a8d2-cc7b5ab878c2', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Hệ số hiệu chỉnh metan của hệ thống xử lý nước thải tập trung. hiếu khí ',
        'Methane correction factor of centralized wastewater treatment system. aerobic', '集中式污水处理系统甲烷校正系数。有氧',
        false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('4881ae09-f57e-44c8-9659-d37260c4287b', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 0.500000, 0.000000, null, null, null, '7776a811-2367-4d58-a8d2-cc7b5ab878c2', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null, 'Hệ số hiệu chỉnh metan của hệ thống tự hoại',
        'Methane correction factor for septic systems', '化粪池系统的甲烷校正系数', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('ed09d575-d7a1-4329-9a41-3cfd08dec3dd', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 0.100000, 0.000000, null, null, null, '7776a811-2367-4d58-a8d2-cc7b5ab878c2', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Hệ số hiệu chỉnh metan của nước thải sinh hoạt không được xử lý. xả ra sông. hồ. biển',
        'Methane correction factor of untreated domestic wastewater discharged into rivers, lakes, seas',
        '未处理的生活污水的甲烷校正因子。排入河流湖。海', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('f468c4e8-267f-415e-b996-af0aa8b950c6', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 0.000000, 0.010000, null, null, null, '7776a811-2367-4d58-a8d2-cc7b5ab878c2', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null, 'Hệ số phát thải', 'Emission factor', '排放因子',
        false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('5cd1f99c-04b7-4d7f-b270-49c7aaca9861', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 0.250000, 0.000000, null, null, null, '1d59fd19-6389-496e-be59-146878c8f491', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null, 'Khả năng phát sinh khí CH₄ tối đa - B0',
        'Maximum CH₄ gas generation potential - B0', '最大 CH₄ 气体生成潜力 - B0', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('1c08eb83-740f-4642-9f90-41b82a983f21', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 0.300000, 0.000000, null, null, null, '1d59fd19-6389-496e-be59-146878c8f491', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Hệ số hiệu chỉnh metan của hệ thống xử lý nước thải tập trung. hiếu khí',
        'Methane correction factor of centralized wastewater treatment system. aerobic', '集中式污水处理系统甲烷校正系数。有氧',
        false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('8956c5ca-8b55-44d3-b51e-cf31e7c31a43', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 0.200000, 0.000000, null, null, null, '1d59fd19-6389-496e-be59-146878c8f491', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Hệ số hiệu chỉnh metan của hệ thống xử lý bán hiếu khí ( kị khí nông)',
        'Methane correction factor of semi-aerobic (aerobic) treatment system', '准好氧（好氧）处理系统甲烷修正系数', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('d619b9bf-17ee-4cfd-8723-a01dce721434', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 0.800000, 0.000000, null, null, null, '1d59fd19-6389-496e-be59-146878c8f491', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null, 'Hệ số hiệu chỉnh metan của hệ thống kỵ khí sâu',
        'Methane correction factor of deep anaerobic system', '深度厌氧系统甲烷校正因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('0dc7e020-4f25-42cd-8ac3-69235ee55995', 0, 'seed_data_sql', '2025-05-28 19:17:50.305066', '2025-05-28 19:17:50.305066',
        'seed_data_sql', 0.000000, 0.100000, 0.000000, null, null, null, '1d59fd19-6389-496e-be59-146878c8f491', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', true, null,
        'Hệ số hiệu chỉnh metan của nước thải sinh hoạt không được xử lý. xả ra sông. hồ. biển',
        'Methane correction factor of untreated domestic wastewater discharged into rivers, lakes, seas',
        '未处理的生活污水的甲烷校正因子。排入河流湖。海', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('e00cdaf3-d661-476e-a43e-cd588a1444e5', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 63.100000, 1.000000, 0.100000, null, 'KILOGRAM', 'TERAJOULE', 'ac6d6897-5702-425d-8c24-e83b8f7dbd34', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, 'bd30dfa7-5f82-45ba-a29c-342ce02efd5c',
        'Hệ số phát thải của khí hóa lỏng', 'Emission factor of blast furnace gas', '高炉煤气的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('019725aa-0b11-4d44-9b8c-add256a4ac07', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 63.100000, 62.000000, 0.200000, null, 'KILOGRAM', 'TERAJOULE', '0a0d6d52-ad15-400a-8a43-5e2993757dad',
        '', '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, 'bd30dfa7-5f82-45ba-a29c-342ce02efd5c',
        'Hệ số phát thải của khí hóa lỏng', 'Emission factor of blast furnace gas', '高炉煤气的排放因子', false);
INSERT INTO emission_factor (id, version, created_by, created_date, last_modified_date, last_modified_by, co2, ch4, n2o,
                             factor_name, unit_numerator, unit_denominator, emission_source_id, description, valid_from, valid_to,
                             is_direct_emission, energy_conversion_id, name_vi, name_en, name_zh, active)
VALUES ('c7c2956b-799d-48b0-a2d2-a68de8262e19', 0, 'seed_data_sql', '2025-05-28 19:17:50.209157', '2025-05-28 19:17:50.209157',
        'seed_data_sql', 63.100000, 5.000000, 0.100000, null, 'KILOGRAM', 'TERAJOULE', 'e1884bed-7223-48a7-9568-b53d0b133727', '',
        '2025-01-01 00:00:00.000000', '2030-01-01 00:00:00.000000', false, 'bd30dfa7-5f82-45ba-a29c-342ce02efd5c',
        'Hệ số phát thải của khí hóa lỏng', 'Emission factor of blast furnace gas', '高炉煤气的排放因子', false);
