ALTER TABLE credit_packages_versions DROP COLUMN valid_from;
ALTER TABLE credit_packages_versions DROP COLUMN valid_to;
ALTER TABLE credit_packages_versions ADD COLUMN active BOOLEAN NOT NULL DEFAULT TRUE;

