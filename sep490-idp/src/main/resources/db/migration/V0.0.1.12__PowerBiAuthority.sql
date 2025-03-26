CREATE TABLE power_bi_authorities
(
    created_date       TIMESTAMP,
    created_by         VARCHAR(255),
    last_modified_date TIMESTAMP,
    last_modified_by   VARCHAR(255),
    id                 UUID    NOT NULL DEFAULT gen_random_uuid(),
    version            INTEGER NOT NULL,
    key                VARCHAR(72),
    note               VARCHAR(255),
    expiration_time    TIMESTAMP,
    user_id            UUID
);
ALTER TABLE power_bi_authorities
    ADD CONSTRAINT power_bi_authorities_pk PRIMARY KEY (id),
    ADD CONSTRAINT power_bi_authorities_key_uk UNIQUE (key),
    ADD CONSTRAINT power_bi_authorities_note_uk UNIQUE (note);
ALTER TABLE power_bi_authorities
    ADD CONSTRAINT power_bi_authorities_user_id_fk FOREIGN KEY (user_id) REFERENCES users (id);
CREATE INDEX power_bi_authorities_user_id_idx ON power_bi_authorities (user_id);