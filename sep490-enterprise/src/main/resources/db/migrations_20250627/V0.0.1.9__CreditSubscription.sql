CREATE TABLE credit_convert_ratio
(
    created_date       TIMESTAMP,
    created_by         VARCHAR(255),
    last_modified_date TIMESTAMP,
    last_modified_by   VARCHAR(255),
    id                 UUID    NOT NULL DEFAULT gen_random_uuid(),
    version            INTEGER NOT NULL,
    ratio              DOUBLE PRECISION,
    convert_type               VARCHAR(255)
);
ALTER TABLE credit_convert_ratio
    ADD CONSTRAINT credit_convert_type_unique UNIQUE(convert_type);

ALTER TABLE transactions
    ADD COLUMN transaction_type VARCHAR(255) NOT NULL,
    ADD COLUMN subscription_id UUID NOT NULL,
    ADD COLUMN amount DOUBLE PRECISION,
    ADD COLUMN months INTEGER,
    ADD COLUMN number_of_devices INTEGER;
ALTER TABLE transactions
    ADD CONSTRAINT pk_subscriptions FOREIGN KEY (subscription_id) REFERENCES subscriptions (id);

ALTER TABLE subscriptions
    ADD COLUMN start_date DATE NOT NULL,
    ADD COLUMN end_date DATE NOT NULL,
    ADD COLUMN max_number_of_devices INTEGER NOT NULL;