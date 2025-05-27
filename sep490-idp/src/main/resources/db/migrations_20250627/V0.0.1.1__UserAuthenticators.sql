CREATE TABLE authenticator
(
    id                  UUID            NOT NULL DEFAULT gen_random_uuid(),
    version             INTEGER         NOT NULL,
    credential_id       VARCHAR(255)    NOT NULL,
    credential_name     VARCHAR(255),
    attestation_object  bytea           NOT NULL,
    sign_count          bigint,
    user_id             UUID            NOT NULL
);
ALTER TABLE authenticator
    ADD CONSTRAINT authenticator_pk PRIMARY KEY (id),
    ADD CONSTRAINT authenticator_credential_id_unique UNIQUE (credential_id),
    ADD CONSTRAINT authenticator_user_id_fk FOREIGN KEY (user_id) REFERENCES users (id);