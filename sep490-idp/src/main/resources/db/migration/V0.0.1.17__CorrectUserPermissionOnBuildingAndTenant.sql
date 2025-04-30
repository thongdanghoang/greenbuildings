ALTER TABLE enterprise_user_building_permissions
    RENAME TO user_authorities;
ALTER TABLE user_authorities
    DROP CONSTRAINT enterprise_user_building_permissions_building_id_user_id_unique;
ALTER TABLE user_authorities
    RENAME COLUMN building_id TO reference_id;
insert into user_authorities (user_id, reference_id, permission)
select u.id, u.enterprise_id, u.user_role
from users u
where u.deleted = false
  and (u.user_role = 'ENTERPRISE_OWNER' or u.user_role = 'TENANT');

insert into user_authorities (user_id, permission)
select u.id, u.user_role
from users u
where u.deleted = false
  and (u.user_role = 'BASIC_USER' or u.user_role = 'SYSTEM_ADMIN');
alter table users
    drop column user_role,
    drop column user_scope,
    drop column enterprise_id;
ALTER TABLE user_authorities
    ADD CONSTRAINT user_authorities_user_id_name_unique
        UNIQUE (user_id, permission);
