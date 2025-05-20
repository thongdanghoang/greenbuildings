UPDATE tenant
SET address = 'No address provided'
WHERE address IS NULL OR TRIM(address) = '';

UPDATE tenant
SET hotline = '0123456789'
WHERE hotline IS NULL OR TRIM(hotline) = '';

UPDATE tenant
SET tax_code = '0000000000'
WHERE tax_code IS NULL OR TRIM(tax_code) = '';

UPDATE tenant
SET business_license_image_url = 'https://greenbuildings.cloud/'
WHERE business_license_image_url IS NULL OR TRIM(business_license_image_url) = '';