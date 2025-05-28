CREATE TABLE emission_source
(
    id               UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    version          INTEGER          NOT NULL,
    name_vi          VARCHAR(255),
    name_en          VARCHAR(255),
    name_zh          VARCHAR(255)
);

CREATE TABLE fuel
(
    id             UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    version        INTEGER          NOT NULL,
    name_vi        VARCHAR(255),
    name_en        VARCHAR(255),
    name_zh        VARCHAR(255)
);

CREATE TABLE energy_conversion
(
    id                          UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    version                     INTEGER          NOT NULL,
    fuel_id                     UUID UNIQUE,
    conversion_value            DECIMAL(20, 6),
    conversion_unit_numerator   VARCHAR(255),
    conversion_unit_denominator VARCHAR(255),
    CONSTRAINT fk_fuel_id FOREIGN KEY (fuel_id) REFERENCES fuel (id)
);

CREATE TABLE emission_factor
(
    id                   UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    version              INT              NOT NULL,
    created_by           VARCHAR(255)     NOT NULL,
    created_date         TIMESTAMP        NOT NULL,
    last_modified_date   TIMESTAMP        NOT NULL,
    last_modified_by     VARCHAR(255)     NOT NULL,
    co2                  DECIMAL(20, 6),
    ch4                  DECIMAL(20, 6),
    n2o                  DECIMAL(20, 6),
    factor_name          BIGINT,
    unit_numerator       VARCHAR(255)     NOT NULL,
    unit_denominator     VARCHAR(255)     NOT NULL,
    emission_source_id   UUID             NOT NULL,
    description          TEXT,
    valid_from           TIMESTAMP        NOT NULL,
    valid_to             TIMESTAMP,
    is_direct_emission   BOOLEAN,
    energy_conversion_id UUID,
    name_vi              VARCHAR(255),
    name_en              VARCHAR(255),
    name_zh              VARCHAR(255),
    CONSTRAINT fk_emission_source_id FOREIGN KEY (emission_source_id) REFERENCES emission_source (id),
    CONSTRAINT fk_energy_conversion_id FOREIGN KEY (energy_conversion_id) REFERENCES energy_conversion (id)
);
CREATE TABLE emission_activity
(
    id                      UUID PRIMARY KEY NOT NULL DEFAULT gen_random_uuid(),
    version                 INT              NOT NULL,
    created_by              VARCHAR(255)     NOT NULL,
    created_date            TIMESTAMP        NOT NULL,
    last_modified_date      TIMESTAMP        NOT NULL,
    last_modified_by        VARCHAR(255)     NOT NULL,
    building_id             UUID             NOT NULL,
    emission_factor_id      UUID,
    emission_source_id      UUID,
    name                    VARCHAR(255)     NOT NULL,
    type                    VARCHAR(255)     NOT NULL,
    category                VARCHAR(255)     NOT NULL,
    quantity                INT              NOT NULL,
    description             VARCHAR(1000),
    CONSTRAINT fk_building_id FOREIGN KEY (building_id) REFERENCES buildings (id),
    CONSTRAINT fk_emission_factor_id FOREIGN KEY (emission_factor_id) REFERENCES emission_factor (id),
    CONSTRAINT fk_emission_source_id FOREIGN KEY (emission_source_id) REFERENCES emission_source (id)
);
CREATE TABLE activity_data_record
(
    id                   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    version              INT,
    created_by           VARCHAR(255) NOT NULL,
    created_date         TIMESTAMP    NOT NULL,
    last_modified_date   TIMESTAMP    NOT NULL,
    last_modified_by     VARCHAR(255) NOT NULL,
    emission_activity_id UUID         NOT NULL,
    value                NUMERIC      NOT NULL,
    unit                 VARCHAR(255) NOT NULL,
    start_date           TIMESTAMP    NOT NULL,
    end_date             TIMESTAMP    NOT NULL,
    CONSTRAINT fk_emission_activity_id FOREIGN KEY (emission_activity_id) REFERENCES emission_activity (id)
);