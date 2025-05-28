create table users
(
    created_date       timestamp,
    created_by         varchar(255),
    last_modified_date timestamp,
    last_modified_by   varchar(255),
    id                 uuid       default gen_random_uuid()          not null,
    version            integer                                       not null,
    password           varchar(72),
    email              varchar(255),
    email_verified     boolean    default false                      not null,
    phone              varchar(16),
    phone_verified     boolean    default false                      not null,
    first_name         varchar(50),
    last_name          varchar(100),
    deleted            boolean    default false                      not null,
    locale             varchar(5) default 'vi-VN'::character varying not null
);

alter table users
    add constraint users_pk
        primary key (id);

alter table users
    add constraint users_email_unique
        unique (email);

create table authenticator
(
    id                 uuid default gen_random_uuid() not null,
    version            integer                        not null,
    credential_id      varchar(255)                   not null,
    credential_name    varchar(255),
    attestation_object bytea                          not null,
    sign_count         bigint,
    user_id            uuid                           not null
);

alter table authenticator
    add constraint authenticator_pk
        primary key (id);

alter table authenticator
    add constraint authenticator_credential_id_unique
        unique (credential_id);

alter table authenticator
    add constraint authenticator_user_id_fk
        foreign key (user_id) references users;

create table user_otp
(
    id           uuid default gen_random_uuid() not null,
    version      integer                        not null,
    user_id      uuid                           not null,
    otp_code     varchar(255)                   not null,
    expired_time timestamp                      not null
);

alter table user_otp
    add constraint pk_user_otp
        primary key (id);

alter table user_otp
    add constraint fk_user_otp_user
        foreign key (user_id) references users;

create table user_authorities
(
    id           uuid    default gen_random_uuid() not null,
    user_id      uuid                              not null,
    reference_id uuid,
    permission   varchar(255)                      not null,
    deleted      boolean default false             not null
);

create index idx_enterprise_user_building_permissions_user_id
    on user_authorities (user_id);

alter table user_authorities
    add constraint enterprise_user_building_permissions_pk
        primary key (id);

alter table user_authorities
    add constraint user_authorities_user_id_name_unique
        unique (user_id, permission);

alter table user_authorities
    add constraint enterprise_user_building_permissions_fk_user
        foreign key (user_id) references users;

create table power_bi_authorities
(
    created_date       timestamp,
    created_by         varchar(255),
    last_modified_date timestamp,
    last_modified_by   varchar(255),
    id                 uuid         default gen_random_uuid() not null,
    version            integer                                not null,
    key                varchar(72),
    note               varchar(255),
    expiration_time    timestamp,
    user_id            uuid,
    scope              varchar(255) default 'ENTERPRISE'::character varying,
    enterprise_id      uuid                                   not null,
    last_used          timestamp
);

create index power_bi_authorities_user_id_idx
    on power_bi_authorities (user_id);

alter table power_bi_authorities
    add constraint power_bi_authorities_pk
        primary key (id);

alter table power_bi_authorities
    add constraint power_bi_authorities_key_uk
        unique (key);

alter table power_bi_authorities
    add constraint power_bi_authorities_note_uk
        unique (note);

alter table power_bi_authorities
    add constraint power_bi_authorities_user_id_fk
        foreign key (user_id) references users;

alter table power_bi_authorities
    add constraint check_scope
        check ((scope)::text = ANY ((ARRAY ['BUILDING'::character varying, 'ENTERPRISE'::character varying])::text[]));

create table power_bi_building_permissions
(
    power_bi_authority_id uuid not null,
    building_id           uuid not null
);

create index idx_power_bi_building_permissions_power_bi_authority_id
    on power_bi_building_permissions (power_bi_authority_id);

alter table power_bi_building_permissions
    add constraint fk_power_bi_authorities_id
        foreign key (power_bi_authority_id) references power_bi_authorities
            on delete cascade;