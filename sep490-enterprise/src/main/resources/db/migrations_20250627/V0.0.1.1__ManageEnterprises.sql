ALTER TABLE enterprises
    ADD COLUMN hotline VARCHAR(20),
    ADD COLUMN name    VARCHAR(255),
    ADD COLUMN email   VARCHAR(255);
ALTER TABLE enterprises
    ALTER COLUMN hotline SET NOT NULL,
    ALTER COLUMN name SET NOT NULL,
    ALTER COLUMN email SET NOT NULL;
ALTER TABLE enterprises
    ADD CONSTRAINT enterprises_email_unique UNIQUE (email);

ALTER TABLE wallets
    ADD COLUMN balance BIGINT;