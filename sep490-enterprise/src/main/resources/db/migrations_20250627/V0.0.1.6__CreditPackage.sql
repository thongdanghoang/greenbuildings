ALTER TABLE payments DROP CONSTRAINT payments_fk_bundles;
ALTER TABLE payments DROP COLUMN bundle_id;
ALTER TABLE bundles RENAME TO credit_packages;

ALTER TABLE credit_packages
    ADD COLUMN number_of_credits INT NOT NULL DEFAULT 0,
    ADD COLUMN price BIGINT NOT NULL DEFAULT 0;
