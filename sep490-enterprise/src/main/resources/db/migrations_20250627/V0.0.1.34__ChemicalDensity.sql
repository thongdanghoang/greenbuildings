CREATE TABLE chemical_density
(
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    version          INTEGER          NOT NULL,
    chemical_formula VARCHAR(255)     NOT NULL,
    value            DOUBLE PRECISION NOT NULL,
    unit_numerator   VARCHAR(50)      NOT NULL,
    unit_denominator VARCHAR(50)      NOT NULL
);

INSERT INTO chemical_density (version, chemical_formula, value, unit_numerator, unit_denominator)
VALUES (1, 'CO2', 1.977, 'KILOGRAM', 'CUBIC_METER'),
       (1, 'CH4', 0.717, 'KILOGRAM', 'CUBIC_METER'),
       (1, 'N2O', 1.978, 'KILOGRAM', 'CUBIC_METER');