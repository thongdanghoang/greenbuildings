CREATE TABLE emission_source
(
    created_date       TIMESTAMP,
    created_by         VARCHAR(255),
    last_modified_date TIMESTAMP,
    last_modified_by   VARCHAR(255),
    id                 UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    version            INTEGER      NOT NULL,
    building_id        UUID         NOT NULL,
    name               VARCHAR(255) NOT NULL, -- e.g., Equipment, Device, Fuel
    type               VARCHAR(255) NOT NULL, -- electricity, fuel, transport
    category           VARCHAR(255) NOT NULL, -- e.g., Floor, Room,
    quantity           INT          NOT NULL,
    description        VARCHAR(1000),
    CONSTRAINT fk_emission_source_building FOREIGN KEY (building_id) REFERENCES buildings (id)
);
CREATE INDEX ix_emission_source_building_id ON emission_source (building_id);

CREATE TABLE emission_factor
(
    created_date                TIMESTAMP,
    created_by                  VARCHAR(255),
    last_modified_date          TIMESTAMP,
    last_modified_by            VARCHAR(255),
    id                          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    version                     INTEGER      NOT NULL,
    co2                         DECIMAL(20, 6),
    ch4                         DECIMAL(20, 6),
    n2o                         DECIMAL(20, 6),
    name                        VARCHAR(255) NOT NULL, -- e.g., Electricity Grid VN 2025
    unit_numerator              VARCHAR(255) NOT NULL,
    unit_denominator            VARCHAR(255),
    source                      VARCHAR(255) NOT NULL, -- e.g., Vietnam MoIT, IPCC
    description                 VARCHAR(1000),
    valid_from                  TIMESTAMP    NOT NULL, -- inclusive
    valid_to                    TIMESTAMP,             -- inclusive
    is_direct_emission          BOOLEAN,
    conversion_value            DECIMAL(20, 6),
    conversion_unit_numerator   VARCHAR(255),
    conversion_unit_denominator VARCHAR(255)
);
ALTER TABLE emission_factor
    ADD CONSTRAINT constraint_net_calorific_values_conversion CHECK ( is_direct_emission = TRUE OR
                                                                      (conversion_value IS NOT NULL AND
                                                                       conversion_unit_numerator IS NOT NULL AND
                                                                       conversion_unit_denominator IS NOT NULL) ),
    ADD CONSTRAINT constraint_co2_ch4_n2o_not_all_null CHECK ( co2 IS NOT NULL OR ch4 IS NOT NULL OR n2o IS NOT NULL );

CREATE TABLE activity_data
(
    created_date       TIMESTAMP,
    created_by         VARCHAR(255),
    last_modified_date TIMESTAMP,
    last_modified_by   VARCHAR(255),
    id                 UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    version            INTEGER        NOT NULL,
    emission_source_id UUID           NOT NULL,
    emission_factor_id UUID           NOT NULL,
    value              DECIMAL(20, 6) NOT NULL, -- e.g., kWh, liters
    unit               VARCHAR(255)   NOT NULL, -- e.g., kWh, liters
    start_date         TIMESTAMP      NOT NULL, -- inclusive
    end_date           TIMESTAMP      NOT NULL, -- inclusive
    CONSTRAINT fk_activity_data_emission_source FOREIGN KEY (emission_source_id) REFERENCES emission_source (id),
    CONSTRAINT fk_activity_data_emission_factor FOREIGN KEY (emission_factor_id) REFERENCES emission_factor (id)
);
CREATE INDEX ix_activity_data_emission_source_id ON activity_data (emission_source_id);
CREATE INDEX ix_activity_data_emission_factor_id ON activity_data (emission_factor_id);
