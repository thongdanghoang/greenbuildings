CREATE TABLE IF NOT EXISTS user_otp
(
    id                  UUID            NOT NULL DEFAULT gen_random_uuid(),
    version             INTEGER         NOT NULL,
    user_id             UUID            NOT NULL,
    otp_code            VARCHAR(255)    NOT NULL,
    expired_time        TIMESTAMP       NOT NULL
);
ALTER TABLE user_otp DROP CONSTRAINT IF EXISTS pk_user_otp;
ALTER TABLE user_otp DROP CONSTRAINT IF EXISTS fk_user_otp_user;

ALTER TABLE user_otp
    ADD CONSTRAINT pk_user_otp PRIMARY KEY (id),
    ADD CONSTRAINT fk_user_otp_user FOREIGN KEY (user_id) REFERENCES users (id);
-- Do not add constraints on otp code length at database side
