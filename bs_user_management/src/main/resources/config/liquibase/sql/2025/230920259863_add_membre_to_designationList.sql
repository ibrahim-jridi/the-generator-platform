INSERT INTO "bs_designations_list"("id", "pm_user_id", "designated_user_id", "role_id", "created_by", "created_date",
                                   "last_modified_by", "last_modified_date", "version", "deleted")

VALUES(gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1111233' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111168' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_TECH' LIMIT 1),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
    1,'false'),
       (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1111233' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111168' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_PROD' LIMIT 1),
        'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
        1,'false');
