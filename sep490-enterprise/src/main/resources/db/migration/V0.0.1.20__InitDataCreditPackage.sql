insert into credit_packages (created_date, created_by, last_modified_date, last_modified_by, id, version, active)
values
    (NOW(),'admin',NOW(),'admin','8c3a84fe-7f4e-4d70-8f83-765b47c81b30',0,true),
    (NOW(),'admin',NOW(),'admin','4e871690-9e94-427d-88bc-e25c81792aab',0,true),
    (NOW(),'admin',NOW(),'admin','69152ed0-8f55-4abc-9c6a-abc4cd13ae4a',0,false),
    (NOW(),'admin',NOW(),'admin','4293f414-b697-4b63-84de-c2c98501edcd',0,true);

insert into credit_packages_versions (price, number_of_credits, id, version, credit_package_id, active)
values
    (15000,500,gen_random_uuid(),0,'4293f414-b697-4b63-84de-c2c98501edcd',true),
    (1000000,100,gen_random_uuid(),0,'8c3a84fe-7f4e-4d70-8f83-765b47c81b30',true),
    (2000000,300,gen_random_uuid(),0,'69152ed0-8f55-4abc-9c6a-abc4cd13ae4a',false),
    (100000,250,gen_random_uuid(),0,'4e871690-9e94-427d-88bc-e25c81792aab',true),
    (100000,100,gen_random_uuid(),0,'4e871690-9e94-427d-88bc-e25c81792aab',false),
    (4000000,400,gen_random_uuid(),0,'69152ed0-8f55-4abc-9c6a-abc4cd13ae4a',true),
    (2000000,400,gen_random_uuid(),0,'69152ed0-8f55-4abc-9c6a-abc4cd13ae4a',false);