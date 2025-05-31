-- 1. Add the column (already done)
alter table assets
    add column code varchar(8);

-- 2. Set random string values for existing records
update assets
set code = upper(substring(md5(random()::text || clock_timestamp()::text) from 1 for 8))
where code is null;

-- 3. Add NOT NULL constraint
alter table assets
    alter column code set not null;

-- 4. Add unique constraint on code, enterprise_id, and tenant_id
alter table assets
    add constraint uk_assets_code_enterprise
        unique (code, enterprise_id),
    add constraint uk_assets_code_tenant
        unique (code, tenant_id);