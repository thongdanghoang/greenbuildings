CREATE TABLE emission_factor (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    version INTEGER NOT NULL,
    emission_source VARCHAR(255) NOT NULL,
    co2 DECIMAL(20,6) DEFAULT 0,
    ch4 DECIMAL(20,6) DEFAULT 0,
    n2o DECIMAL(20,6) DEFAULT 0,
    co2e DECIMAL(20,6) DEFAULT 0,
    is_direct_convert BOOLEAN DEFAULT FALSE,
    input_unit VARCHAR(50) NOT NULL,
    input_value DECIMAL(20,6) DEFAULT 0,
    fuel_type VARCHAR(255) NOT NULL
);

CREATE TABLE emission_activity (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    version INTEGER NOT NULL,
    building_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    data DECIMAL(20,6) NOT NULL,
    emission_factor_id UUID NOT NULL,
    CONSTRAINT fk_emission_activity_building FOREIGN KEY (building_id) REFERENCES buildings(id) ON DELETE CASCADE,
    CONSTRAINT fk_emission_activity_emission_factor FOREIGN KEY (emission_factor_id) REFERENCES emission_factor(id) ON DELETE CASCADE
);

CREATE TABLE machine (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    version INTEGER NOT NULL,
    building_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    quantity INT NOT NULL,
    emission_source VARCHAR(255) NOT NULL,
    CONSTRAINT fk_machine_building FOREIGN KEY (building_id) REFERENCES buildings(id) ON DELETE CASCADE
);

CREATE TABLE machine_fuel_emission (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    version INTEGER NOT NULL,
    machine_id UUID NOT NULL,
    emission_factor_id UUID NOT NULL,
    data DECIMAL(20,6) NOT NULL,
    CONSTRAINT fk_machine_fuel_machine FOREIGN KEY (machine_id) REFERENCES machine(id) ON DELETE CASCADE,
    CONSTRAINT fk_machine_fuel_emission_factor FOREIGN KEY (emission_factor_id) REFERENCES emission_factor(id) ON DELETE CASCADE
);

CREATE TABLE energy_conversion (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    version INTEGER NOT NULL DEFAULT 0,
    fuel_type VARCHAR(255) NOT NULL UNIQUE, -- Enum (FuelType)
    joules_from DECIMAL(20,6), -- Tera Joules
    joules_to DECIMAL(20,6), -- Tera Joules
    is_value_range BOOLEAN DEFAULT FALSE,
    base_unit VARCHAR(255) NOT NULL -- Enum (UnitType)
);


INSERT INTO energy_conversion (fuel_type, joules_from, joules_to, is_value_range, base_unit)
VALUES
    ('CrudeOil', 42, 47, TRUE, 'TERAGRAM'),
    ('Orimulsion', 27.5, 27.5, FALSE, 'TERAGRAM'),
    ('NaturalGasLiquids', 0, 0, FALSE, 'TERAGRAM'),
    ('Ethane', 47.5, 47.5, FALSE, 'TERAGRAM'),
    ('Propane', 46.3, 46.3, FALSE, 'TERAGRAM'),
    ('Butane', 45.5, 45.5, FALSE, 'TERAGRAM'),
    ('MotorGasoline', 44.3, 44.3, FALSE, 'CUBIC_METER'),
    ('AviationGasoline', 44.3, 44.3, FALSE, 'CUBIC_METER'),
    ('JetGasoline', 44.1, 44.1, FALSE, 'CUBIC_METER'),
    ('JetKerosene', 44.1, 44.1, FALSE, 'CUBIC_METER'),
    ('OtherKerosene', 43.8, 43.8, FALSE, 'CUBIC_METER'),
    ('ShaleOil', 40.2, 40.2, FALSE, 'TERAGRAM'),
    ('GasDieselOil', 43.0, 43.0, FALSE, 'CUBIC_METER'),
    ('ResidualFuelOil', 40.4, 40.4, FALSE, 'TERAGRAM'),
    ('LiquefiedPetroleumGas', 47.3, 47.3, FALSE, 'TERAGRAM'),
    ('Naphtha', 44.5, 44.5, FALSE, 'CUBIC_METER'),
    ('NaturalGas', 37.5, 42.3, TRUE, 'MILLION_CUBIC_METER'),
    ('Lubricants', 40.2, 40.2, FALSE, 'GIGAGRAM'),
    ('PetroleumCoke', 32.5, 32.5, FALSE, 'GIGAGRAM'),
    ('RefineryFeedstocks', 43.3, 43.3, FALSE, 'GIGAGRAM'),
    ('RefineryGas', 44.5, 44.5, FALSE, 'GIGAGRAM'),
    ('ParaffinWaxes', 40.0, 40.0, FALSE, 'GIGAGRAM'),
    ('OtherPetroleumProducts', 38.0, 44.0, TRUE, 'GIGAGRAM'),
    ('Anthracite', 30.0, 30.0, FALSE, 'GIGAGRAM'),
    ('CokingCoal', 28.2, 28.2, FALSE, 'GIGAGRAM'),
    ('OtherBituminousCoal', 25.8, 25.8, FALSE, 'GIGAGRAM'),
    ('SubBituminousCoal', 18.9, 18.9, FALSE, 'GIGAGRAM'),
    ('Lignite', 11.9, 11.9, FALSE, 'GIGAGRAM'),
    ('OilShaleAndTarSands', 8.9, 8.9, FALSE, 'GIGAGRAM'),
    ('BrownCoalBriquettes', 20.0, 20.0, FALSE, 'GIGAGRAM'),
    ('PatentFuel', 0.020, 0.020, FALSE, 'MEGAGRAM'),
    ('CokeOvenCoke', 0.0282, 0.0282, FALSE, 'MEGAGRAM'),
    ('GasCoke', 0.0285, 0.0285, FALSE, 'MEGAGRAM'),
    ('CoalTar', 0.0326, 0.0326, FALSE, 'MEGAGRAM'),
    ('GasWorksGas', 0.0176, 0.0176, FALSE, 'THOUSAND_CUBIC_METER'),
    ('CokeOvenGas', 0.0176, 0.0176, FALSE, 'THOUSAND_CUBIC_METER'),
    ('BlastFurnaceGas', 0.0036, 0.0036, FALSE, 'THOUSAND_CUBIC_METER'),
    ('OxygenSteelFurnaceGas', 0.0070, 0.0070, FALSE, 'THOUSAND_CUBIC_METER'),
    ('NaturalGasDry', 0.0480, 0.0480, FALSE, 'MEGAGRAM'),
    ('MunicipalWastesNonBiomass', 0.0100, 0.0100, FALSE, 'MEGAGRAM'),
    ('IndustrialWastes', 0.0150, 0.0150, FALSE, 'MEGAGRAM'),
    ('WasteOils', 0.0402, 0.0402, FALSE, 'MEGAGRAM'),
    ('Peat', 0.0098, 0.0098, FALSE, 'MEGAGRAM'),
    ('WoodWoodWaste', 0.0156, 0.0156, FALSE, 'MEGAGRAM'),
    ('SulphiteLyes', 0.0118, 0.0118, FALSE, 'MEGAGRAM'),
    ('OtherPrimarySolidBiomass', 0.0125, 0.0125, FALSE, 'MEGAGRAM'),
    ('Charcoal', 0.0295, 0.0295, FALSE, 'MEGAGRAM'),
    ('Biogasoline', 0.0270, 0.0270, FALSE, 'MEGAGRAM'),
    ('Biodiesels', 0.0270, 0.0270, FALSE, 'MEGAGRAM'),
    ('OtherLiquidBiofuels', 0.0270, 0.0270, FALSE, 'MEGAGRAM'),
    ('LandfillGas', 0.0190, 0.0190, FALSE, 'THOUSAND_CUBIC_METER'),
    ('SludgeGas', 0.0190, 0.0190, FALSE, 'THOUSAND_CUBIC_METER'),
    ('OtherBiogas', 0.0190, 0.0190, FALSE, 'THOUSAND_CUBIC_METER'),
    ('MunicipalWastesBiomass', 0.0100, 0.0100, FALSE, 'MEGAGRAM'),
    ('MunicipalSolidWaste', 0.009, 0.012, TRUE, 'MEGAGRAM'),
    ('Plastics', 0.032, 0.040, TRUE, 'MEGAGRAM'),
    ('Textiles', 0.010, 0.020, TRUE, 'MEGAGRAM'),
    ('Rubber', 0.03, 0.03, FALSE, 'MEGAGRAM'),
    ('Nappies', 0.015, 0.020, TRUE, 'MEGAGRAM'),
    ('IndustrialSolidWaste', 0.008, 0.015, TRUE, 'MEGAGRAM'),
    ('HazardousWaste', 0, 0, FALSE, 'MEGAGRAM'),
    ('ClinicalWaste', 0.015, 0.025, TRUE, 'MEGAGRAM'),
    ('SewageSludge', 0.010, 0.015, TRUE, 'MEGAGRAM');
