UPDATE "bs_user"
SET denomination = social_reason
WHERE username in ('pm-ext-1111252','pm-ext-1111253','pm-ext-1111254','pm-ext-1111255','pm-ext-1111256','pm-ext-1111257',
                   'pm-ext-1111258','pm-ext-1111259','pm-ext-1111260','pm-ext-1111261','pm-ext-1111262','pm-ext-1111263',
                   'pm-ext-1111264','pm-ext-1111265','pm-ext-1111266','pm-ext-1111267','pm-ext-1111268','pm-ext-1111269',
                   'pm-ext-1111270','pm-ext-1111271','pm-ext-1111272','pm-ext-1111273','pm-ext-1111274','pm-ext-1111275',
                   'pm-ext-1111276','pm-ext-1111277','pm-ext-1111278','pm-ext-1111279','pm-ext-1111280','pm-ext-1111281',
                   'pm-ext-1111282','pm-ext-1111283','pm-ext-1111284','pm-ext-1111285','pm-ext-1111286','pm-ext-1111287',
                   'pm-ext-1111288','pm-ext-1111289','pm-ext-1111290','pm-ext-1111291','pm-ext-1111292','pm-ext-1111293',
                   'pm-ext-1111294');

DELETE FROM "bs_designations_list"
WHERE pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1111223' LIMIT 1)
  AND designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111168' LIMIT 1);
DELETE FROM "bs_designations_list"
WHERE pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1111233' LIMIT 1)
  AND designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111168' LIMIT 1);

DELETE FROM "bs_designations_list"
WHERE pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1111225' LIMIT 1)
  AND designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111224' LIMIT 1);

DELETE FROM "bs_designations_list"
WHERE pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1111234' LIMIT 1)
  AND designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111171' LIMIT 1)
AND role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1);




INSERT INTO "bs_designations_list"("id", "pm_user_id", "designated_user_id", "role_id", "created_by", "created_date",
                                   "last_modified_by", "last_modified_date", "version", "deleted")
VALUES
    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1111233' LIMIT 1),
    (SELECT id FROM bs_user WHERE username = 'ext-1111168' LIMIT 1),
    (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),  1,'false'),
    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1111233' LIMIT 1),
    (SELECT id FROM bs_user WHERE username = 'ext-1111168' LIMIT 1),
    (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),  1,'false'),
    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1111225' LIMIT 1),
    (SELECT id FROM bs_user WHERE username = 'ext-1111225' LIMIT 1),
    (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_CTRL_QA' LIMIT 1),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),  1,'false'),
    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1111234' LIMIT 1),
    (SELECT id FROM bs_user WHERE username = 'ext-1111171' LIMIT 1),
    (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),  1,'false'),
    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1111234' LIMIT 1),
    (SELECT id FROM bs_user WHERE username = 'ext-1111171' LIMIT 1),
    (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),  1,'false');
