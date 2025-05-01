ALTER TABLE tenant ADD COLUMN tax_code VARCHAR(255);
ALTER TABLE tenant
    ADD CONSTRAINT check_tax_code
        CHECK (tax_code ~ '^[0-9]{10}(-[0-9]{3})?$');
ALTER TABLE tenant ADD COLUMN business_license_image_url VARCHAR(255);
ALTER TABLE tenant ADD COLUMN address VARCHAR(255);
ALTER TABLE tenant ADD COLUMN representative_name VARCHAR(255);
ALTER TABLE tenant ADD COLUMN representative_position VARCHAR(255);
ALTER TABLE tenant ADD COLUMN representative_contact VARCHAR(255);