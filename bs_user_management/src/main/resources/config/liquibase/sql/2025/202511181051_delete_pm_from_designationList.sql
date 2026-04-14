DELETE FROM "bs_designations_list"
WHERE pm_user_id = (SELECT id FROM bs_user WHERE username = 'pm-ext-1111242' LIMIT 1)
  AND designated_user_id = (SELECT id FROM bs_user WHERE username = 'ext-1111198' LIMIT 1)
 AND role_id = (SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1);




-- INSERT-DESIGNATIONS_TO_PM_USERS

INSERT INTO "bs_designations_list"("id", "pm_user_id", "designated_user_id", "role_id", "created_by", "created_date",
                                   "last_modified_by", "last_modified_date", "version", "deleted")
VALUES
    (gen_random_uuid(),(SELECT id FROM bs_user WHERE username = 'pm-ext-1111242' LIMIT 1),(SELECT id FROM bs_user WHERE username = 'ext-1111198' LIMIT 1),(SELECT id FROM bs_role WHERE label = 'BS_ROLE_PM_RESP_PHARMACOVIG' LIMIT 1),
    'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),'c2102141-dc4e-4c13-b52e-35e665af9d7e',NOW(),
    1,'false');





--AFFECT ROLE  ECTD TO PP USERS
INSERT INTO "bs_rel_user_role"("user_id", "role_id")

VALUES
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111330' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111331' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111332' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111333' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111335' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1)),
    ((SELECT id FROM "bs_user" WHERE username = 'ext-1111336' LIMIT 1), (SELECT id FROM "bs_role" WHERE label = 'BS_ROLE_ECTD' LIMIT 1));




-- Delete from bs_rel_user_role
DELETE FROM "bs_rel_user_role"
WHERE "user_id" = (
    SELECT "id" FROM "bs_user"
    WHERE username = 'ext-1111334'
);

-- Delete from bs_rel_user_group
DELETE FROM "bs_rel_user_group"
WHERE "user_id" = (
    SELECT "id" FROM "bs_user"
    WHERE username = 'ext-1111334'
);

-- Delete from bs_designations_list
DELETE FROM "bs_designations_list"
WHERE "designated_user_id" = (
    SELECT "id" FROM "bs_user"
    WHERE username = 'ext-1111334'
);

-- Delete from bs_user
DELETE FROM "bs_user"
WHERE username = 'ext-1111334';


---- update user

UPDATE "bs_user"
SET phone_number = '+21699177740'
WHERE username ='ext-1111199';

UPDATE "bs_user"
SET email = 'kaouthar.chelly@teriak.com'
WHERE username ='ext-1111199';


UPDATE "bs_user"
SET phone_number = '20963948'
WHERE username ='pm-ext-1112400';
