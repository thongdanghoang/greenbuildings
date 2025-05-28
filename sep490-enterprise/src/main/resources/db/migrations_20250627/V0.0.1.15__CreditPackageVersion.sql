CREATE TABLE credit_packages_versions
(
    valid_from       TIMESTAMP,
    valid_to        TIMESTAMP,
    price BIGINT NOT NULL DEFAULT 0,
    number_of_credits INT NOT NULL DEFAULT 0,
    id                 UUID    NOT NULL DEFAULT gen_random_uuid(),
    version            INTEGER NOT NULL,
    credit_package_id      UUID    NOT NULL
);
ALTER TABLE credit_packages_versions
    ADD CONSTRAINT credit_packages_versions_pk PRIMARY KEY (id);
ALTER TABLE credit_packages_versions
    ADD CONSTRAINT credit_packages_versions_fk_credit_packages FOREIGN KEY (credit_package_id) REFERENCES credit_packages (id);
ALTER TABLE payments ADD COLUMN credit_packages_versions_id UUID;
ALTER TABLE payments
    ADD CONSTRAINT payments_fk_credit_packages_versions FOREIGN KEY (credit_packages_versions_id) REFERENCES credit_packages_versions (id);

