UPDATE "bs_user"
SET denomination = social_reason
WHERE username in ('pm-ext-1112385','pm-ext-1112386','pm-ext-1112387','pm-ext-1112388','pm-ext-1112389',
                   'pm-ext-1112390','pm-ext-1112391','pm-ext-1112392','pm-ext-1112393',
                   'pm-ext-1112394','pm-ext-1112395','pm-ext-1112396','pm-ext-1112397''pm-ext-1112398');
UPDATE "bs_user"
SET last_name = 'El ghali Krichen'
WHERE username = 'ext-1111298';

DELETE FROM "bs_designations_list"
WHERE pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1111223' LIMIT 1)
  AND designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111168' LIMIT 1);

DELETE FROM "bs_designations_list"
WHERE pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1111237' LIMIT 1)
  AND designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111183' LIMIT 1);

DELETE FROM "bs_designations_list"
WHERE pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1111237' LIMIT 1)
  AND designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111183' LIMIT 1);

DELETE FROM "bs_designations_list"
WHERE pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1112386' LIMIT 1)
  AND designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111291' LIMIT 1);

DELETE FROM "bs_designations_list"
WHERE pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1111243' LIMIT 1)
  AND designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111147' LIMIT 1);

DELETE FROM "bs_designations_list"
WHERE pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1111240' LIMIT 1)
  AND designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111184' LIMIT 1);


INSERT INTO "bs_designations_list"("id", "pm_user_id", "designated_user_id", "role_id", "created_by", "created_date",
                                   "last_modified_by", "last_modified_date", "version", "deleted")
VALUES
    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1111223' LIMIT 1),
    (SELECT id FROM bs_user WHERE username = 'ext-1111168' LIMIT 1),
    (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),  1,'false'),
    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1111223' LIMIT 1),
    (SELECT id FROM bs_user WHERE username = 'ext-1111168' LIMIT 1),
    (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),  1,'false'),
    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1111237' LIMIT 1),
    (SELECT id FROM bs_user WHERE username = 'ext-1111183' LIMIT 1),
    (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),  1,'false'),
    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1111237' LIMIT 1),
    (SELECT id FROM bs_user WHERE username = 'ext-1111183' LIMIT 1),
    (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),  1,'false'),
    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1111241' LIMIT 1),
    (SELECT id FROM bs_user WHERE username = 'ext-1111315' LIMIT 1),
    (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),  1,'false'),
    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1111241' LIMIT 1),
    (SELECT id FROM bs_user WHERE username = 'ext-1111315' LIMIT 1),
    (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),  1,'false'),
    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112386' LIMIT 1),
    (SELECT id FROM bs_user WHERE username = 'ext-1111291' LIMIT 1),
    (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),  1,'false'),
    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112386' LIMIT 1),
    (SELECT id FROM bs_user WHERE username = 'ext-1111291' LIMIT 1),
    (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_CTRL_QA' LIMIT 1),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),  1,'false'),
    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1111243' LIMIT 1),
    (SELECT id FROM bs_user WHERE username = 'ext-1111200' LIMIT 1),
    (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_TECH' LIMIT 1),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),  1,'false'),
    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1111240' LIMIT 1),
    (SELECT id FROM bs_user WHERE username = 'ext-1111190' LIMIT 1),
    (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_TECH' LIMIT 1),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),  1,'false'),
    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1111240' LIMIT 1),
    (SELECT id FROM bs_user WHERE username = 'ext-1111190' LIMIT 1),
    (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_PROD' LIMIT 1),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),  1,'false');
