CREATE TABLE enterprises
(
    created_date       TIMESTAMP,
    created_by         VARCHAR(255),
    last_modified_date TIMESTAMP,
    last_modified_by   VARCHAR(255),
    id                 UUID    NOT NULL DEFAULT gen_random_uuid(),
    version            INTEGER NOT NULL
);
ALTER TABLE enterprises
    ADD CONSTRAINT enterprises_pk PRIMARY KEY (id);

CREATE TABLE buildings
(
    created_date       TIMESTAMP,
    created_by         VARCHAR(255),
    last_modified_date TIMESTAMP,
    last_modified_by   VARCHAR(255),
    id                 UUID    NOT NULL DEFAULT gen_random_uuid(),
    version            INTEGER NOT NULL,
    enterprise_id      UUID    NOT NULL
);
ALTER TABLE buildings
    ADD CONSTRAINT buildings_pk PRIMARY KEY (id);
ALTER TABLE buildings
    ADD CONSTRAINT buildings_fk_enterprises FOREIGN KEY (enterprise_id) REFERENCES enterprises (id);
CREATE INDEX buildings_fk_enterprises_idx ON buildings (enterprise_id);


CREATE TABLE wallets
(
    created_date       TIMESTAMP,
    created_by         VARCHAR(255),
    last_modified_date TIMESTAMP,
    last_modified_by   VARCHAR(255),
    id                 UUID    NOT NULL DEFAULT gen_random_uuid(),
    version            INTEGER NOT NULL,
    enterprise_id      UUID    NOT NULL
);
ALTER TABLE wallets
    ADD CONSTRAINT wallets_pk PRIMARY KEY (id);
ALTER TABLE wallets
    ADD CONSTRAINT wallets_fk_enterprises FOREIGN KEY (enterprise_id) REFERENCES enterprises (id);
CREATE INDEX wallets_fk_enterprises_idx ON wallets (enterprise_id);

CREATE TABLE bundles
(
    created_date       TIMESTAMP,
    created_by         VARCHAR(255),
    last_modified_date TIMESTAMP,
    last_modified_by   VARCHAR(255),
    id                 UUID    NOT NULL DEFAULT gen_random_uuid(),
    version            INTEGER NOT NULL
);
ALTER TABLE bundles
    ADD CONSTRAINT bundles_pk PRIMARY KEY (id);

CREATE TABLE payments
(
    created_date       TIMESTAMP,
    created_by         VARCHAR(255),
    last_modified_date TIMESTAMP,
    last_modified_by   VARCHAR(255),
    id                 UUID    NOT NULL DEFAULT gen_random_uuid(),
    version            INTEGER NOT NULL,
    enterprise_id      UUID    NOT NULL,
    bundle_id          UUID    NOT NULL
);
ALTER TABLE payments
    ADD CONSTRAINT payments_pk PRIMARY KEY (id);
ALTER TABLE payments
    ADD CONSTRAINT payments_fk_enterprises FOREIGN KEY (enterprise_id) REFERENCES enterprises (id);
CREATE INDEX payments_fk_enterprises_idx ON payments (enterprise_id);
ALTER TABLE payments
    ADD CONSTRAINT payments_fk_bundles FOREIGN KEY (bundle_id) REFERENCES bundles (id);
CREATE INDEX payments_fk_bundles_idx ON payments (bundle_id);

CREATE TABLE transactions
(
    created_date       TIMESTAMP,
    created_by         VARCHAR(255),
    last_modified_date TIMESTAMP,
    last_modified_by   VARCHAR(255),
    id                 UUID    NOT NULL DEFAULT gen_random_uuid(),
    version            INTEGER NOT NULL,
    enterprise_id      UUID    NOT NULL,
    building_id        UUID    NOT NULL
);
ALTER TABLE transactions
    ADD CONSTRAINT transactions_pk PRIMARY KEY (id);
ALTER TABLE transactions
    ADD CONSTRAINT transactions_fk_enterprises FOREIGN KEY (enterprise_id) REFERENCES enterprises (id);
CREATE INDEX transactions_fk_enterprises_idx ON transactions (enterprise_id);
ALTER TABLE transactions
    ADD CONSTRAINT transactions_fk_buildings FOREIGN KEY (building_id) REFERENCES buildings (id);
CREATE INDEX transactions_fk_buildings_idx ON transactions (building_id);

CREATE TABLE subscriptions
(
    created_date       TIMESTAMP,
    created_by         VARCHAR(255),
    last_modified_date TIMESTAMP,
    last_modified_by   VARCHAR(255),
    id                 UUID    NOT NULL DEFAULT gen_random_uuid(),
    version            INTEGER NOT NULL,
    building_id        UUID    NOT NULL
);
ALTER TABLE subscriptions
    ADD CONSTRAINT subscriptions_pk PRIMARY KEY (id);
ALTER TABLE subscriptions
    ADD CONSTRAINT subscriptions_fk_buildings FOREIGN KEY (building_id) REFERENCES buildings (id);
CREATE INDEX subscriptions_fk_buildings_idx ON subscriptions (building_id);
