
INSERT INTO "bs_designations_list"("id", "pm_user_id", "designated_user_id", "role_id", "created_by", "created_date",
                                   "last_modified_by", "last_modified_date", "version", "deleted")
VALUES
    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1111221' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111224' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_CTRL_QA' LIMIT 1),
     'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
     1,'false'),

    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1111221' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111224' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1),
     'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
     1,'false');


DELETE FROM "bs_rel_user_role"
WHERE "user_id" IN (
    SELECT "id" FROM "bs_user"
    WHERE username = 'pm-ext-1112317'
)
  AND EXISTS (
    SELECT 1 FROM "bs_user"
    WHERE username = 'pm-ext-1112317'
    );

--delete groups

DELETE FROM "bs_rel_user_group"
WHERE "user_id" IN (
    SELECT "id" FROM "bs_user"
    WHERE username = 'pm-ext-1112317'
)
  AND EXISTS (
    SELECT 1 FROM "bs_user"
    WHERE username = 'pm-ext-1112317'
    );

--delete bs_designations_list

DELETE FROM "bs_designations_list"
WHERE "designated_user_id" IN (
    SELECT "id" FROM "bs_user"
    WHERE username = 'pm-ext-1112317'
)
  AND EXISTS (
    SELECT 1 FROM "bs_user"
    WHERE username = 'pm-ext-1112317'
    );
--delete the old users
DELETE FROM "bs_user"
WHERE username = 'pm-ext-1112317'
  AND EXISTS (
    SELECT 1 FROM "bs_user"
    WHERE username = 'pm-ext-1112317'
    );


-- Update the user's role from 'BS_ROLE_RESP_REG_AFF' to 'BS_ROLE_PM_RESP_PHARMACOVIG'
UPDATE bs_designations_list
SET role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOV' LIMIT 1)
WHERE designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111212' LIMIT 1)
  AND role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_RESP_REG_AFF' LIMIT 1);




UPDATE bs_designations_list
SET designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111224' LIMIT 1)
WHERE designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111220' LIMIT 1)
  AND role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_REG_AFF' LIMIT 1)
  AND pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1111221' LIMIT 1);


----------------------------------------------------------------------------------------------

UPDATE bs_designations_list
SET designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111224' LIMIT 1)
WHERE designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111220' LIMIT 1)
  AND role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_CTRL_QA' LIMIT 1)
  AND pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1111221' LIMIT 1);


