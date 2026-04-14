DELETE FROM "bs_designations_list"
WHERE pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1112391' LIMIT 1)
  AND designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111305' LIMIT 1)
                                                     AND role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_TECH' LIMIT 1);

INSERT INTO "bs_designations_list"("id", "pm_user_id", "designated_user_id", "role_id", "created_by", "created_date",
                                   "last_modified_by", "last_modified_date", "version", "deleted")
VALUES
    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1112391' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111306' LIMIT 1),
    (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_PHARMA_RESP_TECH' LIMIT 1),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),  1,'false');
UPDATE "bs_user"
SET denomination = social_reason
WHERE username in ('pm-ext-1112397','pm-ext-1112398');
