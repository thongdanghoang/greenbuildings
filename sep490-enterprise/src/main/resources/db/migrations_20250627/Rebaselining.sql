drop table wallets cascade;
drop table payments cascade;
drop table transactions cascade;
drop table subscriptions cascade;
drop table credit_convert_ratio cascade;
drop table activity_data_record cascade;
drop table emission_activity cascade;
drop table emission_factor cascade;
drop table emission_source cascade;
drop table energy_conversion cascade;
drop table fuel cascade;
drop table credit_packages_versions cascade;
drop table credit_packages cascade;
drop table record_files cascade;
drop table dashboard cascade;
drop table activity_type cascade;
drop table chemical_density cascade;
drop table invitations cascade;
drop table building_group cascade;
drop table excel_import_files cascade;
drop table assets cascade;
drop table buildings cascade;
drop table enterprises cascade;
drop table tenant cascade;
DO
$$
    DECLARE
        archive_date text := to_char(current_date, 'YYYYMMDD');
        old_table    text := 'flyway_schema_history';
        new_table    text := 'flyway_schema_history_' || archive_date;
        pk_old       text := 'flyway_schema_history_pk';
        pk_new       text := pk_old || '_' || archive_date;
    BEGIN
        RAISE NOTICE 'Date of archive is: %', archive_date;
        RAISE NOTICE 'The % will be renamed to: %', old_table, new_table;

        -- Rename the table
        EXECUTE format('ALTER TABLE %I RENAME TO %I;', old_table, new_table);

        -- Rename the primary key constraint
        RAISE NOTICE 'The primary key will be renamed to: %', pk_new;
        EXECUTE format('ALTER TABLE %I RENAME CONSTRAINT %I TO %I;', new_table, pk_old, pk_new);

    END
$$;
DROP index flyway_schema_history_s_idx;
create table flyway_schema_history
(
    installed_rank integer                 not null,
    version        varchar(50),
    description    varchar(200)            not null,
    type           varchar(20)             not null,
    script         varchar(1000)           not null,
    checksum       integer,
    installed_by   varchar(100)            not null,
    installed_on   timestamp default now() not null,
    execution_time integer                 not null,
    success        boolean                 not null
);
create index flyway_schema_history_s_idx
    on flyway_schema_history (success);
alter table flyway_schema_history
    add constraint flyway_schema_history_pk
        primary key (installed_rank);