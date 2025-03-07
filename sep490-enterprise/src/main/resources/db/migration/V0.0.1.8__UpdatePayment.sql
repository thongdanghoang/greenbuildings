ALTER TABLE payments ADD COLUMN number_of_credits INT;
ALTER TABLE payments ADD COLUMN bin VARCHAR(255);
ALTER TABLE payments ADD COLUMN account_number VARCHAR(255);
ALTER TABLE payments ADD COLUMN account_name VARCHAR(255);
ALTER TABLE payments ADD COLUMN description VARCHAR(1000);
ALTER TABLE payments ADD COLUMN order_code BIGINT;
ALTER TABLE payments ADD COLUMN currency VARCHAR(255);
ALTER TABLE payments ADD COLUMN payment_link_id VARCHAR(1000);
ALTER TABLE payments ADD COLUMN payos_status VARCHAR(255);
ALTER TABLE payments ADD COLUMN expired_at BIGINT;
ALTER TABLE payments ADD COLUMN checkout_url VARCHAR(1000);
ALTER TABLE payments ADD COLUMN qr_code VARCHAR(1000);