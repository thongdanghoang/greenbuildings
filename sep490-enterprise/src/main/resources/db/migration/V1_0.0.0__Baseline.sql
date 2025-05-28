CREATE EXTENSION IF NOT EXISTS btree_gist;
create table enterprises
(
    created_date               timestamp,
    created_by                 varchar(255),
    last_modified_date         timestamp,
    last_modified_by           varchar(255),
    id                         uuid default gen_random_uuid() not null,
    version                    integer                        not null,
    hotline                    varchar(20)                    not null,
    name                       varchar(255)                   not null,
    email                      varchar(255)                   not null,
    tax_code                   varchar(255),
    business_license_image_url varchar(255),
    address                    varchar(255),
    representative_name        varchar(255),
    representative_position    varchar(255),
    representative_contact     varchar(255)
);

alter table enterprises
    add constraint enterprises_pk
        primary key (id);

alter table enterprises
    add constraint enterprises_email_unique
        unique (email);

alter table enterprises
    add constraint check_tax_code
        check ((tax_code)::text ~ '^[0-9]{10}(-[0-9]{3})?$'::text);

create table buildings
(
    created_date       timestamp,
    created_by         varchar(255),
    last_modified_date timestamp,
    last_modified_by   varchar(255),
    id                 uuid             default gen_random_uuid()     not null,
    version            integer                                        not null,
    enterprise_id      uuid                                           not null,
    name               varchar(255)                                   not null,
    latitude           double precision                               not null,
    longitude          double precision                               not null,
    address            varchar(255)     default ''::character varying not null,
    deleted            boolean          default false                 not null,
    number_of_levels   integer          default 0,
    area               double precision default 0
);

create index buildings_fk_enterprises_idx
    on buildings (enterprise_id);

alter table buildings
    add constraint buildings_pk
        primary key (id);

alter table buildings
    add constraint buildings_fk_enterprises
        foreign key (enterprise_id) references enterprises;

create table wallets
(
    created_date       timestamp,
    created_by         varchar(255),
    last_modified_date timestamp,
    last_modified_by   varchar(255),
    id                 uuid default gen_random_uuid() not null,
    version            integer                        not null,
    enterprise_id      uuid                           not null,
    balance            double precision
);

create index wallets_fk_enterprises_idx
    on wallets (enterprise_id);

alter table wallets
    add constraint wallets_pk
        primary key (id);

alter table wallets
    add constraint wallets_fk_enterprises
        foreign key (enterprise_id) references enterprises;

create table credit_packages
(
    created_date       timestamp,
    created_by         varchar(255),
    last_modified_date timestamp,
    last_modified_by   varchar(255),
    id                 uuid    default gen_random_uuid() not null,
    version            integer                           not null,
    active             boolean default true              not null
);

alter table credit_packages
    add constraint bundles_pk
        primary key (id);

create table payments
(
    created_date                timestamp,
    created_by                  varchar(255),
    last_modified_date          timestamp,
    last_modified_by            varchar(255),
    id                          uuid default gen_random_uuid() not null,
    version                     integer                        not null,
    enterprise_id               uuid                           not null,
    status                      varchar(255)                   not null,
    amount                      bigint                         not null,
    number_of_credits           integer,
    bin                         varchar(255),
    account_number              varchar(255),
    account_name                varchar(255),
    description                 varchar(1000),
    order_code                  bigint,
    currency                    varchar(255),
    payment_link_id             varchar(1000),
    payos_status                varchar(255),
    expired_at                  bigint,
    checkout_url                varchar(1000),
    qr_code                     varchar(1000),
    credit_packages_versions_id uuid
);

create index payments_fk_enterprises_idx
    on payments (enterprise_id);

alter table payments
    add constraint payments_pk
        primary key (id);

alter table payments
    add constraint payments_fk_enterprises
        foreign key (enterprise_id) references enterprises;

create table transactions
(
    created_date       timestamp,
    created_by         varchar(255),
    last_modified_date timestamp,
    last_modified_by   varchar(255),
    id                 uuid default gen_random_uuid() not null,
    version            integer                        not null,
    enterprise_id      uuid                           not null,
    building_id        uuid                           not null,
    transaction_type   varchar(255)                   not null,
    subscription_id    uuid                           not null,
    amount             double precision,
    months             integer,
    number_of_devices  integer
);

create index transactions_fk_enterprises_idx
    on transactions (enterprise_id);

create index transactions_fk_buildings_idx
    on transactions (building_id);

alter table transactions
    add constraint transactions_pk
        primary key (id);

alter table transactions
    add constraint transactions_fk_enterprises
        foreign key (enterprise_id) references enterprises;

alter table transactions
    add constraint transactions_fk_buildings
        foreign key (building_id) references buildings;

create table subscriptions
(
    created_date          timestamp,
    created_by            varchar(255),
    last_modified_date    timestamp,
    last_modified_by      varchar(255),
    id                    uuid default gen_random_uuid() not null,
    version               integer                        not null,
    building_id           uuid                           not null,
    start_date            date                           not null,
    end_date              date                           not null,
    max_number_of_devices integer                        not null
);

create index subscriptions_fk_buildings_idx
    on subscriptions (building_id);

alter table subscriptions
    add constraint subscriptions_pk
        primary key (id);

alter table transactions
    add constraint pk_subscriptions
        foreign key (subscription_id) references subscriptions;

alter table subscriptions
    add constraint no_date_overlap
        exclude using gist (building_id with =, daterange(start_date, end_date, '[]'::text) with &&);

alter table subscriptions
    add constraint subscriptions_fk_buildings
        foreign key (building_id) references buildings;

create table credit_convert_ratio
(
    created_date       timestamp,
    created_by         varchar(255),
    last_modified_date timestamp,
    last_modified_by   varchar(255),
    id                 uuid default gen_random_uuid() not null,
    version            integer                        not null,
    ratio              double precision,
    convert_type       varchar(255)
);

alter table credit_convert_ratio
    add constraint credit_convert_type_unique
        unique (convert_type);

create table emission_source
(
    id      uuid default gen_random_uuid() not null,
    version integer                        not null,
    name_vi varchar(255),
    name_en varchar(255),
    name_zh varchar(255)
);

alter table emission_source
    add primary key (id);

create table fuel
(
    id      uuid default gen_random_uuid() not null,
    version integer                        not null,
    name_vi varchar(255),
    name_en varchar(255),
    name_zh varchar(255)
);

alter table fuel
    add primary key (id);

create table energy_conversion
(
    id                          uuid default gen_random_uuid() not null,
    version                     integer                        not null,
    fuel_id                     uuid,
    conversion_value            numeric(20, 6),
    conversion_unit_numerator   varchar(255),
    conversion_unit_denominator varchar(255)
);

alter table energy_conversion
    add primary key (id);

alter table energy_conversion
    add unique (fuel_id);

alter table energy_conversion
    add constraint fk_fuel_id
        foreign key (fuel_id) references fuel;

create table emission_factor
(
    id                   uuid    default gen_random_uuid() not null,
    version              integer                           not null,
    created_by           varchar(255)                      not null,
    created_date         timestamp                         not null,
    last_modified_date   timestamp                         not null,
    last_modified_by     varchar(255)                      not null,
    co2                  numeric(20, 6),
    ch4                  numeric(20, 6),
    n2o                  numeric(20, 6),
    factor_name          bigint,
    unit_numerator       varchar(255),
    unit_denominator     varchar(255),
    emission_source_id   uuid                              not null,
    description          text,
    valid_from           timestamp                         not null,
    valid_to             timestamp,
    is_direct_emission   boolean,
    energy_conversion_id uuid,
    name_vi              varchar(255),
    name_en              varchar(255),
    name_zh              varchar(255),
    active               boolean default false
);

alter table emission_factor
    add primary key (id);

alter table emission_factor
    add constraint fk_emission_source_id
        foreign key (emission_source_id) references emission_source;

alter table emission_factor
    add constraint fk_energy_conversion_id
        foreign key (energy_conversion_id) references energy_conversion;

create table emission_activity
(
    id                 uuid default gen_random_uuid() not null,
    version            integer                        not null,
    created_by         varchar(255)                   not null,
    created_date       timestamp                      not null,
    last_modified_date timestamp                      not null,
    last_modified_by   varchar(255)                   not null,
    emission_factor_id uuid                           not null,
    name               varchar(255)                   not null,
    category           varchar(255)                   not null,
    description        varchar(1000),
    type_id            uuid,
    building_group_id  uuid,
    building_id        uuid                           not null
);

alter table emission_activity
    add primary key (id);

alter table emission_activity
    add constraint fk_emission_factor_id
        foreign key (emission_factor_id) references emission_factor;

alter table emission_activity
    add constraint fk_activity_building
        foreign key (building_id) references buildings;

create table activity_data_record
(
    id                   uuid    default gen_random_uuid() not null,
    version              integer,
    created_by           varchar(255)                      not null,
    created_date         timestamp                         not null,
    last_modified_date   timestamp                         not null,
    last_modified_by     varchar(255)                      not null,
    value                numeric                           not null,
    unit                 varchar(255)                      not null,
    start_date           date                              not null,
    end_date             date                              not null,
    file_id              uuid,
    group_item_id        uuid,
    emission_activity_id uuid                              not null,
    quantity             integer default 0,
    asset_id             uuid
);

create index idx_activity_data_record_asset_id
    on activity_data_record (asset_id);

alter table activity_data_record
    add primary key (id);

alter table activity_data_record
    add constraint activity_data_record_no_date_overlap
        exclude using gist (emission_activity_id with =, COALESCE(asset_id, '00000000-0000-0000-0000-000000000000'::uuid) with =, daterange(start_date, end_date, '[]'::text) with &&);

alter table activity_data_record
    add constraint fk_activity_data_record_emission_activityforeign
        foreign key (emission_activity_id) references emission_activity;

create table credit_packages_versions
(
    price             bigint  default 0                 not null,
    number_of_credits integer default 0                 not null,
    id                uuid    default gen_random_uuid() not null,
    version           integer                           not null,
    credit_package_id uuid                              not null,
    active            boolean default true              not null,
    discount          bigint  default 0                 not null
);

create index credit_packages_versions_fk_credit_packages_idx
    on credit_packages_versions (credit_package_id);

alter table credit_packages_versions
    add constraint credit_packages_versions_pk
        primary key (id);

alter table payments
    add constraint payments_fk_credit_packages_versions
        foreign key (credit_packages_versions_id) references credit_packages_versions;

alter table credit_packages_versions
    add constraint credit_packages_versions_fk_credit_packages
        foreign key (credit_package_id) references credit_packages;

create table record_files
(
    id                 uuid                                not null,
    version            integer   default 0                 not null,
    created_date       timestamp,
    created_by         varchar(255),
    last_modified_date timestamp,
    last_modified_by   varchar(255),
    file_name          varchar(255)                        not null,
    content_type       varchar(100),
    file_size          bigint                              not null,
    minio_path         varchar(512)                        not null,
    upload_date        timestamp default CURRENT_TIMESTAMP not null
);

alter table record_files
    add primary key (id);

alter table activity_data_record
    add constraint fk_activity_data_record_file
        foreign key (file_id) references record_files;

create table dashboard
(
    id            uuid default gen_random_uuid() not null,
    version       integer                        not null,
    title         varchar(255)                   not null,
    src           text                           not null,
    enterprise_id uuid                           not null
);

alter table dashboard
    add unique (title);

create table activity_type
(
    id                 uuid default gen_random_uuid() not null,
    version            integer,
    name               varchar(255)                   not null,
    created_date       timestamp,
    created_by         varchar(255),
    last_modified_date timestamp,
    last_modified_by   varchar(255),
    description        varchar(255),
    building_id        uuid
);

alter table activity_type
    add primary key (id);

alter table emission_activity
    add constraint fk_emission_activity_type
        foreign key (type_id) references activity_type;

alter table activity_type
    add foreign key (building_id) references buildings;

create table chemical_density
(
    id               uuid default gen_random_uuid() not null,
    version          integer                        not null,
    chemical_formula varchar(255)                   not null,
    value            double precision               not null,
    unit_numerator   varchar(50)                    not null,
    unit_denominator varchar(50)                    not null
);

alter table chemical_density
    add primary key (id);

create table tenant
(
    id                         uuid default gen_random_uuid() not null,
    name                       varchar(255),
    version                    integer                        not null,
    created_date               timestamp,
    created_by                 varchar(255),
    last_modified_date         timestamp,
    last_modified_by           varchar(255),
    email                      varchar(255)                   not null,
    hotline                    varchar(255),
    tax_code                   varchar(255),
    business_license_image_url varchar(255),
    address                    varchar(255),
    representative_name        varchar(255),
    representative_position    varchar(255),
    representative_contact     varchar(255)
);

alter table tenant
    add primary key (id);

alter table tenant
    add unique (email);

alter table tenant
    add constraint check_tax_code
        check ((tax_code)::text ~ '^[0-9]{10}(-[0-9]{3})?$'::text);

create table building_group
(
    id                  uuid default gen_random_uuid() not null,
    version             integer                        not null,
    created_date        timestamp,
    last_modified_date  timestamp,
    created_by          varchar(255),
    last_modified_by    varchar(255),
    building_group_name varchar(255),
    building_id         uuid                           not null,
    tenant_id           uuid,
    description         text
);

alter table building_group
    add primary key (id);

alter table emission_activity
    add constraint fk_building_group
        foreign key (building_group_id) references building_group;

alter table building_group
    add foreign key (building_id) references buildings;

alter table building_group
    add foreign key (tenant_id) references tenant;

create table invitations
(
    id                 uuid default gen_random_uuid() not null,
    version            integer                        not null,
    building_group_id  uuid                           not null,
    email              varchar(255)                   not null,
    status             varchar(50),
    created_date       timestamp,
    created_by         varchar(255),
    last_modified_date timestamp,
    last_modified_by   varchar(255)
);

alter table invitations
    add primary key (id);

alter table invitations
    add foreign key (building_group_id) references building_group;

create table excel_import_files
(
    id                 uuid                                not null,
    version            integer   default 0                 not null,
    created_date       timestamp,
    created_by         varchar(255),
    last_modified_date timestamp,
    last_modified_by   varchar(255),
    file_name          varchar(255)                        not null,
    content_type       varchar(100),
    file_size          bigint                              not null,
    minio_path         varchar(512)                        not null,
    upload_date        timestamp default CURRENT_TIMESTAMP not null,
    type               varchar(255)                        not null
);

alter table excel_import_files
    add primary key (id);

create table assets
(
    id                 uuid default gen_random_uuid() not null,
    version            integer                        not null,
    name               varchar(255)                   not null,
    building_id        uuid,
    disabled           boolean,
    description        varchar(255),
    created_date       timestamp,
    created_by         varchar(255),
    last_modified_date timestamp,
    last_modified_by   varchar(255),
    enterprise_id      uuid,
    tenant_id          uuid
);

create index idx_assets_building_id
    on assets (building_id);

create index idx_assets_enterprise_id
    on assets (enterprise_id);

create index idx_assets_tenant_id
    on assets (tenant_id);

alter table assets
    add primary key (id);

alter table activity_data_record
    add foreign key (asset_id) references assets;

alter table assets
    add foreign key (building_id) references buildings;

alter table assets
    add foreign key (enterprise_id) references enterprises;

alter table assets
    add foreign key (tenant_id) references tenant;

alter table assets
    add constraint chk_tenant_enterprise_nulls
        check (((tenant_id IS NULL) AND (enterprise_id IS NOT NULL)) OR ((tenant_id IS NOT NULL) AND (enterprise_id IS NULL)));